package good.damn.engine.opengl.shaders

import android.opengl.GLES30
import good.damn.engine.models.MGMInformatorShader
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.base.MGShaderBase

class MGShaderLightPass
: MGShaderSingleModeInstanced(),
MGIShaderCameraPosition,
MGIShaderLight {

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

    override val lightDirectional = MGShaderLightDirectional()

    override val lightPoints = Array(
        MGMInformatorShader.SIZE_LIGHT_POINT
    ) {
        MGShaderLightPoint(it)
    }

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

        lightPoints.forEach {
            it.setupUniforms(
                program
            )
        }
    }
}