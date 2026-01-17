package good.damn.engine.utils

import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import good.damn.common.utils.COUtilsInputStream
import good.damn.engine.MGEngine
import good.damn.engine.MGMountDirectory
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

object MGUtilsShader {
    private val CHARSET_UTF8 = Charset.forName(
        "UTF-8"
    )

    @JvmStatic
    fun loadString(
        file: File,
        buffer: ByteArray
    ): String {
        val inp = FileInputStream(
            file
        )

        val b = COUtilsInputStream.readBytes(
            inp,
            buffer
        )

        inp.close()

        return String(
            b,
            CHARSET_UTF8
        )
    }

    @JvmStatic
    fun loadString(
        path: String,
        buffer: ByteArray
    ): String {
        val pubFile = File(
            MGMountDirectory.DIRECTORY,
            path
        )

        if (!pubFile.exists()) {
            throw Exception(pubFile.path)
        }

        return loadString(
            pubFile,
            buffer
        )
    }

    @JvmStatic
    fun createProgramFromAssets(
        vertexPath: String,
        fragmentPath: String,
        buffer: ByteArray
    ): Int {
        return createProgram(
            loadString(
                vertexPath,
                buffer
            ),
            loadString(
                fragmentPath,
                buffer
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