package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.util.Log
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBuffer
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MGLandscape(
    width: Int,
    height: Int,
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

    private lateinit var mVertexBuffer: FloatBuffer
    private lateinit var mIndicesBuffer: IntBuffer

    private val mVertexArray = MGArrayVertex()

    init {
        setResolution(
            program,
            width,
            height
        )
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

        mVertexBuffer = MGUtilsBuffer.allocateFloat(
            gridLen * 8
        ) // position(3), texCoord(2), normal(3)

        mIndicesBuffer = MGUtilsBuffer.allocateInt(
            gridLen * 6
        )

        val time = System.currentTimeMillis()
        Log.d(TAG, "init: $time")
        for (z in 0..mHeight) {
            textureX = 0f
            val fz = z.toFloat()
            for (x in 0..mWidth) {
                val fx = x.toFloat()

                createVertex(
                    fz,
                    0.0f,
                    fx,
                    textureX,
                    textureY
                )

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

                mIndicesBuffer.put(leftTop)
                mIndicesBuffer.put(leftBottom)
                mIndicesBuffer.put(rightBottom)
                mIndicesBuffer.put(leftTop)
                mIndicesBuffer.put(rightTop)
                mIndicesBuffer.put(rightBottom)
            }
        }

        mVertexBuffer.position(0)
        mIndicesBuffer.position(0)
        
        mVertexArray.configure(
            program,
            mVertexBuffer,
            mIndicesBuffer
        )
    }

    fun displace(
        map: MGMapDisplace
    ) {
        val c = mVertexBuffer.capacity()

        var index = 0

        var x: Int
        var z: Int

        while(index < c) {
            x = mVertexBuffer[index].toInt()
            z = mVertexBuffer[index + 2].toInt()

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

            // Normal X
            mVertexBuffer.put(
                index + 5,
                rightVert - leftVert
            )

            // Normal Y
            mVertexBuffer.put(
                index + 6,
                1.0f
            )

            // Normal Z
            mVertexBuffer.put(
                index + 7,
                bottomVert - topVert
            )

            mVertexBuffer.put(
                index + 1, map.getHeightRatio(
                    x,
                    z,
                    mWidth,
                    mHeight
                )
            )
            index += 8
        }
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

    private inline fun createVertex(
        x: Float,
        y: Float,
        z: Float,
        tx: Float, // Texture coords
        ty: Float
    ) {
        // Position
        mVertexBuffer.put(x)
        mVertexBuffer.put(y)
        mVertexBuffer.put(z)

        // TexCoords
        mVertexBuffer.put(tx)
        mVertexBuffer.put(ty)

        // Normals
        mVertexBuffer.put(0.0f)
        mVertexBuffer.put(1.0f)
        mVertexBuffer.put(0.0f)
    }
}