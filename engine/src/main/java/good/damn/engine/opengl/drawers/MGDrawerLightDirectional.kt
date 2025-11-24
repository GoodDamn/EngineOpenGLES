package good.damn.engine.opengl.drawers

import android.opengl.GLES30.glUniform3f
import android.util.Log
import good.damn.engine.sdk.MGVector3
import good.damn.engine.opengl.shaders.MGShaderLightDirectional

class MGDrawerLightDirectional
: MGIDrawerShader<MGShaderLightDirectional> {

    val ambColor = MGVector3(
        0.1f,
        0.1f,
        0.1f
    )

    private val mPosition = MGVector3(
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

    override fun draw(
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