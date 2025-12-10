package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import androidx.annotation.CallSuper
import good.damn.engine.opengl.shaders.base.MGShaderBase

class MGShaderOpaqueDefer(
    override val materials: Array<MGShaderMaterial>
): MGShaderBase(),
MGIShaderNormal,
MGIShaderCamera,
MGIShaderCameraPosition,
MGIShaderMaterial {

    final override var uniformCameraPosition = 0
        private set

    final override var uniformNormalMatrix = 0
        private set

    final override var uniformCameraProjection = 0
        private set

    final override var uniformCameraView = 0
        private set

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        materials.forEach {
            it.setupUniforms(
                program
            )
        }

        // Uniforms
        uniformCameraProjection = glGetUniformLocation(
            program,
            "projection"
        )

        uniformCameraView = glGetUniformLocation(
            program,
            "view"
        )

        uniformNormalMatrix = glGetUniformLocation(
            program,
            "normalMatrix"
        )

        uniformCameraPosition = glGetUniformLocation(
            program,
            "cameraPosition"
        )
    }
}