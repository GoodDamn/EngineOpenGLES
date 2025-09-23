package good.damn.engine.opengl.drawers

import android.opengl.GLES30.glUniform1f
import android.opengl.GLES30.glUniform3f
import android.util.Log
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.shaders.MGShaderLightDirectional

class MGDrawerLightDirectional(
    var shader: MGShaderLightDirectional
): MGIDrawer {

    var ambient = 0.08f
    val ambColor = MGVector(
        0.75f,
        0.75f,
        0.75f
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


    override fun draw() {
        glUniform3f(
            shader.uniformColor,
            .149f,
            .407f,
            .631f
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