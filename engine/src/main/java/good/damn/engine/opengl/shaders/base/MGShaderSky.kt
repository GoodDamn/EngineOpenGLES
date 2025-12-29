package good.damn.engine.opengl.shaders.base

import android.opengl.GLES30.glGetUniformLocation
import good.damn.engine.opengl.shaders.MGIShaderMaterial
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderProjectionViewTexture
import good.damn.engine.opengl.shaders.MGShaderTexture
import java.lang.reflect.Array

class MGShaderSky
: MGShaderProjectionViewTexture(),
MGIShaderModel,
MGIShaderMaterial {

    final override var uniformModelView = 0
        private set

    final override val materials = arrayOf(
        MGShaderMaterial(
            arrayOf(
                MGShaderTexture(
                    "targetTexture"
                )
            )
        )
    )

    override fun setupUniforms(
        program: Int
    ) {
        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )

        materials.forEach {
            it.setupUniforms(
                program
            )
        }

        super.setupUniforms(
            program
        )
    }

}