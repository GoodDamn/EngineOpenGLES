package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGMaterial(
    private val materialTexture: MGMaterialTexture
): MGIDrawerTexture<MGShaderMaterial> {
    var shine = 1f

    fun getTextureByType(
        type: MGEnumTextureType
    ) = materialTexture.getTextureByType(
        type
    )

    override fun draw(
        shader: MGShaderMaterial
    ) {
        GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )

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