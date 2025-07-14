package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.util.Log
import good.damn.engine.opengl.camera.BaseCamera
import good.damn.engine.opengl.maps.DisplacementMap
import good.damn.engine.opengl.textures.Texture
import good.damn.engine.utils.BufferUtils
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.random.Random

class Landscape(
    private val mProgram: Int,
    camera: BaseCamera
): Mesh(
    mProgram,
    camera
) {
    companion object {
        private const val TAG = "Landscape"
    }

    var texture = Texture(
        "textures/grass.jpg",
        mProgram,
        wrapMode = GL_REPEAT
    )

    var material = Material(
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

    override fun draw() {
        super.draw()

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

        mPositionBuffer = BufferUtils
            .allocateFloat(
                gridLen * 3
            )

        mNormalBuffer = BufferUtils
            .allocateFloat(
                gridLen * 3
            )

        mTexCoordBuffer = BufferUtils
            .allocateFloat(
                gridLen * 2
            )

        mIndicesBuffer = BufferUtils
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
        map: DisplacementMap
    ) {
        val c = mPositionBuffer.capacity()

        var i = 1

        var x: Int
        var z: Int

        while(i < c) {
            x = mPositionBuffer[i-1].toInt()
            z = mPositionBuffer[i+1].toInt()

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

    private fun createVertex(
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

        // Normal
        mNormalBuffer.put(0.0f)
        mNormalBuffer.put(1.0f)
        mNormalBuffer.put(0.0f)
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