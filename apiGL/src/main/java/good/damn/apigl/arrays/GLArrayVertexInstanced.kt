package good.damn.apigl.arrays

import android.opengl.GLES30
import android.opengl.GLES30.GL_ARRAY_BUFFER
import android.opengl.GLES30.GL_FLOAT
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glBindBuffer
import android.opengl.GLES30.glBindVertexArray
import android.opengl.GLES30.glDrawElementsInstanced
import android.opengl.GLES30.glEnableVertexAttribArray
import android.opengl.GLES30.glVertexAttribDivisor
import android.opengl.GLES30.glVertexAttribPointer
import java.nio.FloatBuffer

class GLArrayVertexInstanced(
    private val configurator: GLArrayVertexConfigurator
) {

    companion object {
        const val INDEX_ATTRIB_INSTANCE_MODEL = 3
        const val INDEX_ATTRIB_INSTANCE_ROTATION = 7

        const val INDEX_BUFFER_MODEL = 0
        const val INDEX_BUFFER_ROTATION = 1
        private const val STRIDE = 64
    }

    private var meshCount = 0
    private val mBuffers = intArrayOf(1, 1)

    fun setupMatrixBuffer(
        meshCount: Int,
        modelBuffer: FloatBuffer,
        rotationBuffer: FloatBuffer
    ) {
        this.meshCount = meshCount
        val size = meshCount * STRIDE
        GLES30.glGenBuffers(
            mBuffers.size,
            mBuffers,
            0
        )

        glBindVertexArray(
            configurator.vertexArrayId
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            mBuffers[INDEX_BUFFER_MODEL]
        )

        GLES30.glBufferData(
            GL_ARRAY_BUFFER,
            size,
            modelBuffer,
            GLES30.GL_STATIC_DRAW
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            mBuffers[INDEX_BUFFER_ROTATION]
        )

        GLES30.glBufferData(
            GL_ARRAY_BUFFER,
            size,
            rotationBuffer,
            GLES30.GL_STATIC_DRAW
        )
    }

    fun setupInstanceDrawing(
        indexAttrib: Int,
        indexBuffer: Int
    ) {
        glBindVertexArray(
            configurator.vertexArrayId
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            mBuffers[indexBuffer]
        )

        val strideVector4 = 16
        val pos0 = indexAttrib
        val pos1 = indexAttrib + 1
        val pos2 = indexAttrib + 2
        val pos3 = indexAttrib + 3

        attributePointer(
            pos0,
            0
        )

        attributePointer(
            pos1,
            strideVector4
        )

        attributePointer(
            pos2,
            strideVector4*2
        )

        attributePointer(
            pos3,
            strideVector4*3
        )

        glVertexAttribDivisor(
            pos0, 1
        )

        glVertexAttribDivisor(
            pos1, 1
        )

        glVertexAttribDivisor(
            pos2, 1
        )

        glVertexAttribDivisor(
            pos3, 1
        )

        glBindBuffer(
            GL_ARRAY_BUFFER,
            0
        )

        glBindVertexArray(0)
    }

    fun drawInstanced(
        mode: Int = GL_TRIANGLES
    ) {
        glBindVertexArray(
            configurator.vertexArrayId
        )

        glDrawElementsInstanced(
            mode,
            configurator.indicesCount,
            configurator.config.type,
            0,
            meshCount
        )

        glBindVertexArray(0)
    }

    private inline fun attributePointer(
        pos: Int,
        offset: Int
    ) {
        glEnableVertexAttribArray(pos)
        glVertexAttribPointer(
            pos,
            4,
            GL_FLOAT,
            false,
            STRIDE,
            offset
        )
    }

}