package good.damn.engine.opengl.entities

import android.opengl.GLES30
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.drawers.MGIUniform

class MGMaterial
: MGIDrawer,
MGIUniform {

    var specular = 0f
    var shine = 1f
    var lightIntensity = 0.2f

    private var mUniformSpecular = 0
    private var mUniformShininess = 0
    private var mUniformIntensityLight = 0

    override fun setupUniforms(
        program: Int
    ) {
        mUniformSpecular = GLES30.glGetUniformLocation(
            program,
            "specularIntensity"
        )

        mUniformShininess = GLES30.glGetUniformLocation(
            program,
            "shine"
        )

        mUniformIntensityLight = GLES30.glGetUniformLocation(
            program,
            "light.intensity"
        )
    }

    override fun draw() {
        GLES30.glUniform1f(
            mUniformShininess,
            shine
        )

        GLES30.glUniform1f(
            mUniformSpecular,
            specular
        )

        GLES30.glUniform1f(
            mUniformIntensityLight,
            lightIntensity
        )
    }

}