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

    override var uniformTexture = 0
        private set

    override var uniformModelView = 0
        private set

    override var uniformTextureOffset = 0
        private set

    override var uniformCameraPosition = 0
        private set

    override var uniformNormalMatrix = 0
        private set

    override var uniformCameraProjection = 0
        private set

    override var uniformCameraView = 0
        private set

    override val light = MGShaderLightDirectional()
    override val material = MGShaderMaterial()


    override fun setupUniforms(
        program: Int
    ) {
        light.setupUniforms(
            program
        )

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

        uniformTextureOffset = glGetUniformLocation(
            program,
            "textureOffset"
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