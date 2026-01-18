package good.damn.apigl.shaders.base

import android.opengl.GLES20
import android.opengl.GLES30.*
import android.util.Log
import good.damn.apigl.shaders.GLIShader
import good.damn.apigl.utils.GLUtilsShader
import java.io.File

abstract class GLShaderBase
: GLIShader {
    companion object {
        private const val TAG = "MGShaderBase"
    }

    private var mProgram = 0

    override fun use() {
        glUseProgram(
            mProgram
        )
    }

    fun compileFromSource(
        srcVertex: String,
        srcFragment: String,
        binderAttribute: GLBinderAttribute
    ): Int {
        mProgram = GLUtilsShader.createProgram(
            srcVertex,
            srcFragment
        )

        binderAttribute.bindAttributes(
            mProgram
        )

        return 0
    }

    fun compile(
        buffer: ByteArray,
        fileVertex: File,
        fileFragment: File,
        binderAttribute: GLBinderAttribute
    ): Int {
        mProgram = GLUtilsShader.createProgramFromAssets(
            fileVertex,
            fileFragment,
            buffer
        )

        binderAttribute.bindAttributes(
            mProgram
        )

        return 0
    }


    fun link() {
        glLinkProgram(
            mProgram
        )

        val status = intArrayOf(1)
        GLES20.glGetProgramiv(
            mProgram,
            GL_LINK_STATUS,
            status,
            0
        )
        Log.d(TAG, "link: STATUS: ${status[0]}")

        if (status[0] == GL_TRUE) {
            return
        }
        Log.d(TAG, "link: ERROR:")
    }

    fun setupFromSource(
        srcVertex: String,
        srcFragment: String,
        binderAttribute: GLBinderAttribute
    ) {
        compileFromSource(
            srcVertex,
            srcFragment,
            binderAttribute
        )
        link()
        setupUniforms(
            mProgram
        )
    }

    fun setup(
        buffer: ByteArray,
        fileVertex: File,
        fileFragment: File,
        binderAttribute: GLBinderAttribute
    ) {
        compile(
            buffer,
            fileVertex,
            fileFragment,
            binderAttribute
        )
        link()
        setupUniforms(
            mProgram
        )
    }

}