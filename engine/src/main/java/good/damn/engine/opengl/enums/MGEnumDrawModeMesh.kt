package good.damn.engine.opengl.enums

import android.opengl.GLES30

enum class MGEnumDrawModeMesh(
    val v: Int
) {
    TRIANGLES(
        GLES30.GL_TRIANGLES
    ),
    LINES(
        GLES30.GL_LINES
    )
}