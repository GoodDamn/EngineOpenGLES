package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture

class MGMaterial(
    var shader: MGShaderMaterial,
    var textureDiffuse: MGTexture,
    var textureMetallic: MGTexture
): MGIDrawer {
    var shine = 1f

    override fun draw() {
        GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )

        textureDiffuse.draw()
        textureMetallic.draw()
    }

    fun unbind() {
        textureDiffuse.unbind()
        textureMetallic.unbind()
    }

}