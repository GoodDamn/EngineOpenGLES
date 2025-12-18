package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformBlockIndex
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUniformBlockBinding
import androidx.annotation.CallSuper
import good.damn.engine.opengl.shaders.base.MGShaderBase

open class MGShaderSingleModeInstanced
: MGShaderBase() {

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