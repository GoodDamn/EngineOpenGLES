package good.damn.apigl.shaders

import android.opengl.GLES30.glGetUniformBlockIndex
import android.opengl.GLES30.glUniformBlockBinding
import androidx.annotation.CallSuper
import good.damn.apigl.shaders.base.GLShaderBase

open class GLShaderProjectionView
: GLShaderBase() {

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        glUniformBlockBinding(
            program,
            glGetUniformBlockIndex(
                program,
                "Camera"
            ),
            0
        )
    }
}