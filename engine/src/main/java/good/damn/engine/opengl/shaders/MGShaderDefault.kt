package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformLocation

class MGShaderDefault
: MGShaderBase(),
MGIShaderNormal,
MGIShaderCamera,
MGIShaderCameraPosition,
MGIShaderLight,
MGIShaderTexture,
MGIShaderModel {

    companion object {
        private const val NUM_LIGHTS = 4
    }

    override var uniformTexture = 0
        private set

    override var uniformModelView = 0
        private set

    override var uniformCameraPosition = 0
        private set

    override var uniformNormalMatrix = 0
        private set

    override var uniformCameraProjection = 0
        private set

    override var uniformCameraView = 0
        private set

    override val lightDirectional = MGShaderLightDirectional()
    override val material = MGShaderMaterial()
    override val lightPoints = Array(
        NUM_LIGHTS
    ) { MGShaderLightPoint(it) }

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
        uniformTexture = glGetUniformLocation(
            program,
            "texture"
        )

        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )

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