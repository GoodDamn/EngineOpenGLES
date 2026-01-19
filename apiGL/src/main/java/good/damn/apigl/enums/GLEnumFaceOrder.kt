package good.damn.apigl.enums

import android.opengl.GLES30

enum class GLEnumFaceOrder(
    val v: Int
) {
    COUNTER_CLOCK_WISE(
        GLES30.GL_CCW
    ),
    CLOCK_WISE(
        GLES30.GL_CW
    )
}