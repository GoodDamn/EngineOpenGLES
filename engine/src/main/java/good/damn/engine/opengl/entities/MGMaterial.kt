package good.damn.engine.opengl.entities

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderMaterial.Companion.singleMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGMMaterialShader

class MGMaterial(
    private val materialTexture: MGMaterialTexture
): MGIDrawerTexture<MGShaderMaterial> {

    var shine = 1f

    override fun draw(
        shader: MGShaderMaterial
    ) {
        /*GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )*/

        materialTexture.draw(
            shader.textures
        )
    }

    override fun unbind(
        shader: MGShaderMaterial
    ) {
        materialTexture.unbind(
            shader.textures
        )
    }

}