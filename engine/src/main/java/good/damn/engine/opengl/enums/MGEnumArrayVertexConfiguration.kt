package good.damn.engine.opengl.enums

import android.opengl.GLES30

enum class MGEnumArrayVertexConfiguration(
    val type: Int,
    val indicesSize: Int
) {
    BYTE(
        GLES30.GL_UNSIGNED_BYTE,
        1
    ),
    SHORT(
        GLES30.GL_UNSIGNED_SHORT,
        2
    ),
    INT(
        GLES30.GL_UNSIGNED_INT,
        4
    )
}