package good.damn.apigl.drawers

import good.damn.apigl.shaders.GLShaderMaterial

class GLMaterial(
    private val materialTexture: GLDrawerMaterialTexture
): GLIDrawerTexture<GLShaderMaterial> {

    var shine = 1f

    override fun draw(
        shader: GLShaderMaterial
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
        shader: GLShaderMaterial
    ) {
        materialTexture.unbind(
            shader.textures
        )
    }

}