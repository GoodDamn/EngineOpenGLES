package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGMaterial(
    var shader: MGShaderMaterial
): MGIDrawer {

    var specular = 0f
    var shine = 1f
    var lightIntensity = 0.2f

    override fun draw() {
        GLES30.glUniform1f(
            shader.uniformShininess,
            shine
        )

        GLES30.glUniform1f(
            shader.uniformSpecular,
            specular
        )

        GLES30.glUniform1f(
            shader.uniformIntensityLight,
            lightIntensity
        )
    }

}