package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import androidx.annotation.CallSuper

open class MGShaderProjectionViewModel
: MGShaderProjectionView(),
MGIShaderModel {

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