package good.damn.opengles_engine.opengl.light

import java.nio.IntBuffer
import android.opengl.GLES30.*
import android.opengl.Matrix
import good.damn.opengles_engine.opengl.Vector
import good.damn.opengles_engine.opengl.entities.DimensionObject

class DirectionalLight(
    program: Int
): DimensionObject() {

    var ambient = 0.8f

    private val mUniformColor: Int
    private val mUniformAmbient: Int
    private val mUniformDirection: Int

    private val mDirectionVector = Vector(
        1f,
        1f,
        -100f
    )

    init {
        mDirectionVector.normalize()

        mUniformColor = glGetUniformLocation(
            program,
            "light.color"
        )

        mUniformAmbient = glGetUniformLocation(
            program,
            "light.ambient"
        )

        mUniformDirection = glGetUniformLocation(
            program,
            "light.direction"
        )
    }

    override fun setPosition(
        x: Float,
        y: Float,
        z: Float
    ) {
        mDirectionVector.x = x
        mDirectionVector.y = y
        mDirectionVector.z = z
        mDirectionVector.normalize()
    }

    fun draw() {
        glUniform3f(
            mUniformColor,
            1f,
            1f,
            1f
        )

        glUniform1f(
            mUniformAmbient,
            ambient
        )

        glUniform3f(
            mUniformDirection,
            mDirectionVector.x,
            mDirectionVector.y,
            mDirectionVector.z,
        )
    }

}