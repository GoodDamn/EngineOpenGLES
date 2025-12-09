package good.damn.engine.opengl.shaders

import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.base.MGShaderBase

class MGShaderGeometryPass(
    val materials: Array<MGShaderMaterial>
): MGShaderSingleModeInstanced() {
    
    override fun setupUniforms(
        program: Int
    ) {
        super.setupUniforms(
            program
        )

        materials.forEach {
            it.setupUniforms(
                program
            )
        }
    }
}