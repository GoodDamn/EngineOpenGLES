package good.damn.engine.opengl.entities

import android.opengl.GLES30

class MGMaterial(
    program: Int
) {

    var specular = 0f
    var shine = 1f
    var lightIntensity = 0.2f

    private val mUniformSpecular = GLES30.glGetUniformLocation(
        program,
        "specularIntensity"
    )
    private val mUniformShininess = GLES30.glGetUniformLocation(
        program,
        "shine"
    )

    private val mUniformIntensityLight = GLES30.glGetUniformLocation(
        program,
        "light.intensity"
    )

    fun draw() {
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