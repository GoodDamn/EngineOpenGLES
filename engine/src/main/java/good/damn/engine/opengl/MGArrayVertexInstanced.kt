package good.damn.engine.opengl

import android.opengl.GLES30
import android.opengl.GLES30.GL_FLOAT
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.GL_UNSIGNED_INT
import android.opengl.GLES30.glBindVertexArray
import android.opengl.GLES30.glDrawElementsInstanced
import android.opengl.GLES30.glEnableVertexAttribArray
import android.opengl.GLES30.glVertexAttribDivisor
import android.opengl.GLES30.glVertexAttribPointer
import java.nio.FloatBuffer

class MGArrayVertexInstanced
: MGArrayVertex() {

    companion object {
        private const val INDEX_MODEL_INSTANCE = 3
        private const val STRIDE = 64
    }

    private var meshCount = 0

    fun setupMatrixBuffer(
        meshCount: Int,
        buffer: FloatBuffer
    ) {
        this.meshCount = meshCount
        val buffers = intArrayOf(1)
        GLES30.glGenBuffers(
            1,
            buffers,
            0
        )

        GLES30.glBindBuffer(
            GLES30.GL_ARRAY_BUFFER,
            buffers[0]
        )

        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            meshCount * STRIDE,
            buffer,
            GLES30.GL_STATIC_DRAW
        )
    }

    fun setupInstanceDrawing() {
        glBindVertexArray(
            mVertexArray[0]
        )

        val strideVector4 = 16
        val pos0 = INDEX_MODEL_INSTANCE
        val pos1 = INDEX_MODEL_INSTANCE + 1
        val pos2 = INDEX_MODEL_INSTANCE + 2
        val pos3 = INDEX_MODEL_INSTANCE + 3

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

        glBindVertexArray(0)
    }

    fun drawInstanced(
        mode: Int = GL_TRIANGLES
    ) {
        glBindVertexArray(
            mVertexArray[0]
        )

        glDrawElementsInstanced(
            mode,
            mIndicesSize,
            GL_UNSIGNED_INT,
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