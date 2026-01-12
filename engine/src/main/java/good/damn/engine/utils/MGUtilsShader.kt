package good.damn.engine.utils

import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log

object MGUtilsShader {

    @JvmStatic
    fun createProgramFromAssets(
        vertexPath: String,
        fragmentPath: String
    ): Int {
        return createProgram(
            MGUtilsAsset.loadString(
                vertexPath
            ),
            MGUtilsAsset.loadString(
                fragmentPath
            )
        )
    }

    @JvmStatic
    fun createProgram(
        vertex: String,
        fragment: String
    ): Int {
        val program = GLES20.glCreateProgram()
        val frag = createShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragment
        )

        val vert = createShader(
            GLES20.GL_VERTEX_SHADER,
            vertex
        )

        GLES20.glAttachShader(
            program,
            frag
        )

        GLES20.glAttachShader(
            program,
            vert
        )

        return program
    }

    @JvmStatic
    fun createShader(
        type: Int,
        source: String
    ): Int {
        val shader = GLES20.glCreateShader(
            type
        )

        GLES20.glShaderSource(
            shader,
            source
        )

        GLES20.glCompileShader(
            shader
        )

        val status = intArrayOf(0)
        GLES20.glGetShaderiv(
            shader,
            GLES20.GL_COMPILE_STATUS,
            status,
            0
        )

        Log.d("TAG", "createShader: STATUS: ${status[0]} TYPE: $type;")
        if (status[0] == GLES20.GL_FALSE) {
            Log.d("TAG", "createShader: NOT COMPILED: ${GLES30.glGetShaderInfoLog(shader)}")
            return -1
        }

        return shader
    }
}