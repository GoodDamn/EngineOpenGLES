package good.damn.engine.opengl.drawers

import android.opengl.GLES30.glUniform3f
import android.util.Log
import good.damn.engine.sdk.MGVector3
import good.damn.engine.opengl.shaders.MGShaderLightDirectional
import good.damn.engine.sdk.models.SDMLight

class MGDrawerLightDirectional
: MGIDrawerShader<MGShaderLightDirectional> {

    val info = SDMLight(
        MGVector3(
            0.1f,
            0.1f,
            0.1f
        ),
        MGVector3(
            .119f,
            .377f,
            .601f
        ),
        MGVector3(
            1f,
            1f,
            -100f
        )
    )

    init {
        info.normalDirection.normalize()
    }

    fun setPosition(
        xx: Float,
        yy: Float,
        zz: Float
    ) {
        info.normalDirection.run { 
            x = xx
            y = yy
            z = zz
            normalize()
        }
    }

    fun drawColor(
        shader: Int
    ) {
        glUniform3f(
            shader,
            info.colorLight.x,
            info.colorLight.y,
            info.colorLight.z
        )
    }

    override fun draw(
        shader: MGShaderLightDirectional
    ) {
        drawColor(
            shader.uniformColor
        )

        glUniform3f(
            shader.uniformColorAmbient,
            info.colorAmbient.x,
            info.colorAmbient.y,
            info.colorAmbient.z
        )

        glUniform3f(
            shader.uniformDirection,
            info.normalDirection.x,
            info.normalDirection.y,
            info.normalDirection.z,
        )
    }
}