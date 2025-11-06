package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation
import androidx.annotation.CallSuper

open class MGShaderOpaque
: MGShaderBase(),
MGIShaderNormal,
MGIShaderCamera,
MGIShaderCameraPosition,
MGIShaderLight {

    companion object {
        private const val NUM_LIGHTS = 4
    }

    final override var uniformCameraPosition = 0
        private set

    final override var uniformNormalMatrix = 0
        private set

    final override var uniformCameraProjection = 0
        private set

    final override var uniformCameraView = 0
        private set

    final override val lightDirectional = MGShaderLightDirectional()
    final override val material = MGShaderMaterial()
    final override val lightPoints = Array(
        NUM_LIGHTS
    ) { MGShaderLightPoint(it) }

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        lightDirectional.setupUniforms(
            program
        )

        lightPoints.forEach {
            it.setupUniforms(
                program
            )
        }

        material.setupUniforms(
            program
        )


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