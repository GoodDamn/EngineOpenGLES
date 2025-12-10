package good.damn.engine.opengl.shaders

import android.opengl.GLES30
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.base.MGShaderBase

class MGShaderLightPass
: MGShaderSingleModeInstanced(),
MGIShaderCameraPosition {

    val texturePosition = MGShaderTexture(
        "gPosition"
    )

    val textureColorSpec = MGShaderTexture(
        "gColorSpec"
    )

    val textureNormal = MGShaderTexture(
        "gNormal"
    )

    val textureMisc = MGShaderTexture(
        "gMisc"
    )

    override var uniformCameraPosition = 0
        private set

    val lightDirectional = MGShaderLightDirectional()

    override fun setupUniforms(
        program: Int
    ) {
        super.setupUniforms(
            program
        )

        lightDirectional.setupUniforms(
            program
        )

        texturePosition.setupUniforms(
            program
        )

        textureColorSpec.setupUniforms(
            program
        )

        textureNormal.setupUniforms(
            program
        )

        textureMisc.setupUniforms(
            program
        )

        uniformCameraPosition = GLES30.glGetUniformLocation(
            program,
            "cameraPosition"
        )
    }
}