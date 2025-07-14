package good.damn.engine.utils

import android.opengl.GLES20

class ShaderUtils {
    companion object {

        fun createProgramFromAssets(
            vertexPath: String,
            fragmentPath: String
        ): Int {
            return createProgram(
                AssetUtils.loadString(
                    vertexPath
                ),
                AssetUtils.loadString(
                    fragmentPath
                )
            )
        }

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

            return shader
        }
    }
}