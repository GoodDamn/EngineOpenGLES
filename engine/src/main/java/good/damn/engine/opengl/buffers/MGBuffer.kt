package good.damn.engine.opengl.buffers

import android.opengl.GLES30

class MGBuffer(
    private val type: Int
) {
    val id: Int
        get() = mId[0]

    private val mId = intArrayOf(1)

    fun generate() {
        GLES30.glGenBuffers(
            1,
            mId,
            0
        )
    }

    fun bind() {
        GLES30.glBindBuffer(
            type,
            mId[0]
        )
    }

    fun unbind() {
        GLES30.glBindBuffer(
            type,
            0
        )
    }

    fun delete() {
        GLES30.glDeleteBuffers(
            1,
            mId,
            0
        )
    }

}