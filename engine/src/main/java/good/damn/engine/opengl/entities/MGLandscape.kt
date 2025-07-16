package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.util.Log
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
    }

    var texture = MGTexture(
        "textures/grass.jpg",
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
        mTextureOffset = 6.0f
    }

    override fun draw(
        camera: MGCamera
    ) {
        super.draw(
            camera
        )

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

        val time = System.currentTimeMillis()
        Log.d(TAG, "init: $time")
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

        bufferVertex.position(0)
        bufferIndices.position(0)
        
        mVertexArray.configure(
            program,
            bufferVertex,
            bufferIndices
        )
    }

    fun displace(
        map: MGMapDisplace
    ) {
        val c = mVertexArray.sizeVertexArray

        var index = 0

        val data = MGUtilsBuffer.allocateFloat(8)
        mVertexArray.bindVertexBuffer()
        while(index < c) {
            changeVertexData(
                map,
                index,
                data
            )

            index += 8
        }

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

    private fun changeVertexData(
        map: MGMapDisplace,
        index: Int,
        data: FloatBuffer
    ) {
        val x = mVertexArray[index].toInt()
        val z = mVertexArray[index + 2].toInt()

        val topVert = map.getHeightNormalRatio(
            x,
            z - 1,
            mWidth,
            mHeight
        )

        val leftVert = map.getHeightNormalRatio(
            x - 1,
            z,
            mWidth,
            mHeight
        )

        val bottomVert = map.getHeightNormalRatio(
            x,
            z + 1,
            mWidth,
            mHeight
        )

        val rightVert = map.getHeightNormalRatio(
            x + 1,
            z,
            mWidth,
            mHeight
        )

        // Position
        data.put(
            0, mVertexArray[index]
        )
        data.put(
            1, map.getHeightRatio(
                x,
                z,
                mWidth,
                mHeight
            )
        )

        data.put(
            2,
            mVertexArray[index + 2]
        )

        // Texture coords
        data.put(
            3,
            mVertexArray[index + 3]
        )
        data.put(
            4,
            mVertexArray[index + 4]
        )
        // Normal X
        data.put(
            5,
            rightVert - leftVert
        )
        // Normal Y
        data.put(
            6,
            1.0f
        )
        // Normal Z
        data.put(
            7,
            bottomVert - topVert
        )
        data.position(0)
        mVertexArray.changeVertexBufferData(
            index,
            data
        )
    }
}