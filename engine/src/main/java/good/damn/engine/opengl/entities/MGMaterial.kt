package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGMaterial(
    var shader: MGShaderMaterial
): MGIDrawer {

    var specular = 1f
    var shine = 1f

    override fun draw() {
        GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )

        GLES30.glUniform1f(
            shader.uniformSpecular,
            specular
        )
    }

}