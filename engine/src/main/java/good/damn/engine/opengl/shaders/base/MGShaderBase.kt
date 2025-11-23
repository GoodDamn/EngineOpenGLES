package good.damn.engine.opengl.shaders.base

import android.opengl.GLES20
import android.opengl.GLES30.*
import android.util.Log
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.utils.MGUtilsShader

abstract class MGShaderBase
: MGIShader {
    companion object {
        private const val TAG = "MGShaderBase"
    }

    private var mProgram = 0

    override fun use() {
        glUseProgram(
            mProgram
        )
    }

    fun compile(
        pathVertex: String,
        pathFragment: String,
        binderAttribute: MGBinderAttribute
    ): Int {
        mProgram = MGUtilsShader.createProgramFromAssets(
            pathVertex,
            pathFragment
        )

        binderAttribute.bindAttributes(
            mProgram
        )

        return 0
    }

    fun setupUniforms() {
        setupUniforms(
            mProgram
        )
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

    fun setup(
        pathVertex: String,
        pathFragment: String,
        binderAttribute: MGBinderAttribute
    ) {
        compile(
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