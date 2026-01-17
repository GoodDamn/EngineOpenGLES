package good.damn.apigl.shaders.base

import android.opengl.GLES20
import android.opengl.GLES30.*
import android.util.Log
import good.damn.apigl.shaders.GLIShader

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
        mProgram = MGUtilsShader.createProgram(
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
        pathVertex: String,
        pathFragment: String,
        binderAttribute: GLBinderAttribute
    ): Int {
        mProgram = MGUtilsShader.createProgramFromAssets(
            pathVertex,
            pathFragment,
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
        pathVertex: String,
        pathFragment: String,
        binderAttribute: GLBinderAttribute
    ) {
        compile(
            buffer,
            pathVertex,
            pathFragment,
            binderAttribute
        )
        link()
        setupUniforms(
            mProgram
        )
    }

}