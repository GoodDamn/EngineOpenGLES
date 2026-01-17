package good.damn.apigl.drawers

import android.opengl.GLES30.glUniform3f
import good.damn.apigl.shaders.GLShaderLightDirectional
import good.damn.engine.sdk.SDVector3
import good.damn.engine.sdk.models.SDMLight

class GLDrawerLightDirectional
: GLIDrawerShader<GLShaderLightDirectional> {

    val info = SDMLight(
        SDVector3(
            0.1f,
            0.1f,
            0.1f
        ),
        SDVector3(
            .119f,
            .377f,
            .601f
        ),
        SDVector3(
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


    override fun draw(
        shader: GLShaderLightDirectional
    ) {
        glUniform3f(
            shader.uniformColor,
            info.colorLight.x,
            info.colorLight.y,
            info.colorLight.z
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