package good.damn.engine.opengl.drawers

import android.opengl.GLES30.glUniform1f
import android.opengl.GLES30.glUniform3f
import android.util.Log
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.shaders.MGShaderLightDirectional

class MGDrawerLightDirectional {

    val ambColor = MGVector(
        0.1f,
        0.1f,
        0.1f
    )

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
        mPosition.normalize()
        Log.d("TAG", "setPosition: $mPosition")
    }

    fun draw(
        shader: MGShaderLightDirectional
    ) {
        glUniform3f(
            shader.uniformColor,
            .119f,
            .377f,
            .601f
        )

        glUniform3f(
            shader.uniformColorAmbient,
            ambColor.x,
            ambColor.y,
            ambColor.z
        )

        glUniform3f(
            shader.uniformDirection,
            mPosition.x,
            mPosition.y,
            mPosition.z,
        )
    }
}