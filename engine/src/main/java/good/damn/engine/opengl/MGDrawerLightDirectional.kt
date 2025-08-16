package good.damn.engine.opengl

import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUniform1f
import android.opengl.GLES30.glUniform3f

class MGDrawerLightDirectional
: MGIDrawer,
MGIUniform {

    var ambient = 0.08f

    private var mUniformColor = 0
    private var mUniformAmbient = 0
    private var mUniformPosition = 0
    private var mUniformIntensity = 0

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

    override fun setupUniforms(
        program: Int
    ) {
        mUniformColor = glGetUniformLocation(
            program,
            "light.color"
        )

        mUniformAmbient = glGetUniformLocation(
            program,
            "light.factorAmbient"
        )

        mUniformPosition = glGetUniformLocation(
            program,
            "light.position"
        )

        mUniformIntensity = glGetUniformLocation(
            program,
            "light.intensity"
        )
    }

    override fun draw() {
        glUniform3f(
            mUniformColor,
            1f,
            1f,
            1f
        )

        glUniform1f(
            mUniformIntensity,
            2.0f
        )

        glUniform1f(
            mUniformAmbient,
            ambient
        )

        glUniform3f(
            mUniformPosition,
            mPosition.x,
            mPosition.y,
            mPosition.z,
        )
    }
}