package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import androidx.annotation.CallSuper

open class MGShaderSingleModeInstanced
: MGShaderBase(),
MGIShaderCamera {

    final override var uniformCameraProjection = 0
        private set

    final override var uniformCameraView = 0
        private set

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        uniformCameraProjection = glGetUniformLocation(
            program,
            "projection"
        )

        uniformCameraView = glGetUniformLocation(
            program,
            "view"
        )
    }
}