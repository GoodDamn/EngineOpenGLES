package good.damn.engine.opengl.drawers

import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUniform1f
import android.opengl.GLES30.glUniform3f
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.shaders.MGShaderLight

class MGDrawerLightDirectional(
    var shader: MGShaderLight
): MGIDrawer {

    var ambient = 0.08f

    private val mPosition = MGVector(
        1f,
        1f,
        -100f
    )

    init {
        mPosition.normalize()
    }

    fun setPosition(
        x: Float,
        y: Float,
        z: Float
    ) {
        mPosition.x = x
        mPosition.y = y
        mPosition.z = z
    }


    override fun draw() {
        glUniform3f(
            shader.uniformColor,
            1f,
            1f,
            1f
        )

        glUniform1f(
            shader.uniformIntensity,
            2.0f
        )

        glUniform1f(
            shader.uniformAmbient,
            ambient
        )

        glUniform3f(
            shader.uniformPosition,
            mPosition.x,
            mPosition.y,
            mPosition.z,
        )
    }
}