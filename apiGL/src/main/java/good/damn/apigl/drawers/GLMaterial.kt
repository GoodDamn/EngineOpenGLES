package good.damn.apigl.drawers

import good.damn.engine.opengl.shaders.MGShaderMaterial

class GLMaterial(
    private val materialTexture: GLMaterialTexture
): good.damn.apigl.drawers.GLIDrawerTexture<MGShaderMaterial> {

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