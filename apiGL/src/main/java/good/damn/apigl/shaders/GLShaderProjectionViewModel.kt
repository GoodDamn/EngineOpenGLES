package good.damn.apigl.shaders

import android.opengl.GLES30.glGetUniformLocation
import androidx.annotation.CallSuper

open class GLShaderProjectionViewModel
: GLShaderProjectionView(),
    GLIShaderModel {

    final override var uniformModelView = 0
        private set

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        super.setupUniforms(
            program
        )

        // Uniforms
        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )
    }
}