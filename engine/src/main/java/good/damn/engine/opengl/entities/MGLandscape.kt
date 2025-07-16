package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.util.Log
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBuffer
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

class MGLandscape(
    private val mProgram: Int
): MGMesh(
    mProgram
) {
    companion object {
        private const val TAG = "Landscape"
    }

    var texture = MGTexture(
        "textures/grass.jpg",
        mProgram,
        wrapMode = GL_REPEAT
    )

    var material = MGMaterial(
        mProgram
    )

    private var mWidth = 1
    private var mHeight = 1

    private lateinit var mNormalBuffer: FloatBuffer
    private lateinit var mTexCoordBuffer: FloatBuffer
    private lateinit var mPositionBuffer: FloatBuffer
    private lateinit var mIndicesBuffer: IntBuffer

    init {
        setResolution(5,5)
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


        mAttrPosition = glGetAttribLocation(
            mProgram,
            "position"
        )

        mAttrTexCoord = glGetAttribLocation(
            mProgram,
            "texCoord"
        )

        mAttrNormal = glGetAttribLocation(
            mProgram,
            "normal"
        )

        enableVertex(
            mAttrPosition,
            3,
            mPositionBuffer
        )

        enableVertex(
            mAttrTexCoord,
            2,
            mTexCoordBuffer
        )

        enableVertex(
            mAttrNormal,
            3,
            mNormalBuffer
        )

        glDrawElements(
            GL_TRIANGLES,
            mIndicesBuffer.capacity(),
            GL_UNSIGNED_INT,
            mIndicesBuffer
        )

        glDisableVertexAttribArray(
            mAttrPosition
        )

        glDisableVertexAttribArray(
            mAttrTexCoord
        )

        glDisableVertexAttribArray(
            mAttrNormal
        )
    }

    fun setResolution(
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

        mPositionBuffer = MGUtilsBuffer
            .allocateFloat(
                gridLen * 3
            )

        mNormalBuffer = MGUtilsBuffer
            .allocateFloat(
                gridLen * 3
            )

        mTexCoordBuffer = MGUtilsBuffer
            .allocateFloat(
                gridLen * 2
            )

        mIndicesBuffer = MGUtilsBuffer
            .allocateInt(
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
        Log.d(TAG, "init: DELTA_TIME: ${System.currentTimeMillis() - time}")

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

        mPositionBuffer.position(0)
        mNormalBuffer.position(0)
        mTexCoordBuffer.position(0)
        mIndicesBuffer.position(0)
    }

    fun displace(
        map: MGMapDisplace
    ) {
        val c = mPositionBuffer.capacity()

        var i = 1

        var x: Int
        var z: Int

        while(i < c) {
            x = mPositionBuffer[i-1].toInt()
            z = mPositionBuffer[i+1].toInt()

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
            mNormalBuffer.put(
                i - 1,
                rightVert - leftVert
            )

            // Normal Y
            mNormalBuffer.put(
                i,
                1.0f
            )

            // Normal Z
            mNormalBuffer.put(
                i+1,
                bottomVert - topVert
            )

            mPositionBuffer.put(
                i, map.getHeightRatio(
                    x,
                    z,
                    mWidth,
                    mHeight
                )
            )
            i += 3
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
        mPositionBuffer.put(x)
        mPositionBuffer.put(y)
        mPositionBuffer.put(z)

        // TexCoords
        mTexCoordBuffer.put(tx)
        mTexCoordBuffer.put(ty)
    }

    private fun enableVertex(
        attr: Int,
        size: Int,
        buffer: Buffer
    ) {
        glEnableVertexAttribArray(
            attr
        )

        glVertexAttribPointer(
            attr,
            size,
            GL_FLOAT,
            false,
            size * 4,
            buffer
        )
    }

}