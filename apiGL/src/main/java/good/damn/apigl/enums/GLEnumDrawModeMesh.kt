package good.damn.apigl.enums

import android.opengl.GLES30

enum class GLEnumDrawModeMesh(
    val v: Int
) {
    TRIANGLES(
        GLES30.GL_TRIANGLES
    ),
    LINES(
        GLES30.GL_LINE_LOOP
    )
}