package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBuffer
import java.nio.FloatBuffer

class MGLandscape(
    program: Int
): MGMesh(
    program
) {
    companion object {
        private const val TAG = "Landscape"
        private const val MAX_HEIGHT = 255f
    }

    var texture = MGTexture(
        "textures/terrain.png",
        program,
        wrapMode = GL_REPEAT
    )

    var material = MGMaterial(
        program
    )

    private var mWidth = 1
    private var mHeight = 1

    private val mVertexArray = MGArrayVertex()

    init {
        mTextureOffset = 1.0f
    }

    override fun draw(
        camera: MGCamera
    ) {
        super.draw(
            camera
        )

        if (MGEngine.isWireframe) {
            mVertexArray.draw(
                GL_LINES
            )
            return
        }
        texture.draw()
        material.draw()
        mVertexArray.draw()
    }

    fun setResolution(
        program: Int,
        width: Int,
        height: Int
    ) {
        mWidth = width
        mHeight = height

        val dgx = 1.0f / mWidth
        val dgy = 1.0f / mHeight

        var textureX: Float
        var textureY = 0f

        val gridLen = (width+1) * (height+1)

        val bufferVertex = MGUtilsBuffer.allocateFloat(
            gridLen * 8
        ) // position(3), texCoord(2), normal(3)

        val bufferIndices = MGUtilsBuffer.allocateInt(
            gridLen * 6
        )

        var time = System.currentTimeMillis()
        for (z in 0..mHeight) {
            textureX = 0f
            val fz = z.toFloat()
            for (x in 0..mWidth) {
                val fx = x.toFloat()

                // Position
                bufferVertex.put(fz)
                bufferVertex.put(0.0f)
                bufferVertex.put(fx)

                // TexCoords
                bufferVertex.put(textureX)
                bufferVertex.put(textureY)

                // Normals
                bufferVertex.put(0.0f)
                bufferVertex.put(1.0f)
                bufferVertex.put(0.0f)

                textureX += dgx
            }

            textureY += dgy
        }
        Log.d(TAG, "setResolution: BUFFER_VERTEX: ${System.currentTimeMillis() - time}")
        time = System.currentTimeMillis()

        var leftTop: Int
        var leftBottom: Int
        var rightTop: Int
        var rightBottom: Int

        val ww = mWidth + 1

        for (y in 0 until mHeight) {
            for (x in 0 until mWidth) {
                leftTop = x + y * ww
                leftBottom = leftTop + ww

                rightTop = leftTop + 1
                rightBottom = leftBottom + 1

                bufferIndices.put(leftTop)
                bufferIndices.put(leftBottom)
                bufferIndices.put(rightBottom)
                bufferIndices.put(leftTop)
                bufferIndices.put(rightTop)
                bufferIndices.put(rightBottom)
            }
        }

        Log.d(TAG, "setResolution: BUFFER_INDICES: ${System.currentTimeMillis() - time}")

        bufferVertex.position(0)
        bufferIndices.position(0)

        time = System.currentTimeMillis()
        mVertexArray.configure(
            program,
            bufferVertex,
            bufferIndices
        )
        Log.d(TAG, "setResolution: CONFIGURE: ${System.currentTimeMillis() - time}")
    }

    fun displace(
        map: MGMapDisplace
    ) {
        val c = mVertexArray.sizeVertexArray

        var index = 0

        var time = System.currentTimeMillis()
        mVertexArray.bindVertexBuffer()
        while(index < c) {
            changeVertexData(
                map,
                index
            )

            index += 8
        }

        Log.d(TAG, "displace: changeVertexData: ${System.currentTimeMillis() - time}")
        time = System.currentTimeMillis()
        mVertexArray.sendVertexBufferData()
        Log.d(TAG, "displace: changeVertexData: send to GPU ${System.currentTimeMillis() - time}")

        mVertexArray.unbindVertexBuffer()
    }

    override fun setScale(
        x: Float,
        y: Float,
        z: Float
    ) {
        super.setScale(x, y, z)
        setPosition(
            mWidth * -0.5f * x,
            0f,
            mHeight * -0.5f * z
        )
    }

    private inline fun changeVertexData(
        map: MGMapDisplace,
        index: Int
    ) {
        val x = mVertexArray[index].toInt()
        val z = mVertexArray[index + 2].toInt()
        val topVert = map.getHeightNormalRatio(
            x, z - 1,
            mWidth, mHeight
        )

        val leftVert = map.getHeightNormalRatio(
            x - 1, z,
            mWidth, mHeight
        )

        val bottomVert = map.getHeightNormalRatio(
            x, z + 1,
            mWidth, mHeight
        )

        val rightVert = map.getHeightNormalRatio(
            x + 1, z,
            mWidth, mHeight
        )

        val middleVert = map.getHeightNormalRatio(
            x, z,
            mWidth, mHeight
        )

        val smooth = (
            middleVert + topVert + rightVert + leftVert + bottomVert
        ) / 5f
        // Position Y
        mVertexArray.writeVertexBufferData(
            index + 1,
            smooth * MAX_HEIGHT
        )

        // Normal X
        mVertexArray.writeVertexBufferData(
            index + 5,
            rightVert - leftVert
        )

        // Normal Y
        mVertexArray.writeVertexBufferData(
            index+6,
            1.0f
        )
        // Normal Z
        mVertexArray.writeVertexBufferData(
            index+7,
            bottomVert - topVert
        )
    }
}