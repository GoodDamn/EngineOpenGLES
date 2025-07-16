package good.damn.engine.opengl.light

import android.opengl.GLES30.*
import good.damn.engine.opengl.MGMeshStatic
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.entities.MGObjectDimension

class MGLightDirectional(
    program: Int
): MGObjectDimension() {

    var ambient = 0.9f

    private val mUniformColor: Int
    private val mUniformAmbient: Int
    private val mUniformPosition: Int
    private val mUniformIntensity: Int

    private val mPosition = MGVector(
        1f,
        1f,
        -100f
    )

    init {
        mPosition.normalize()

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

    override fun setPosition(
        x: Float,
        y: Float,
        z: Float
    ) {
        mPosition.x = x
        mPosition.y = y
        mPosition.z = z
    }

    fun draw() {
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