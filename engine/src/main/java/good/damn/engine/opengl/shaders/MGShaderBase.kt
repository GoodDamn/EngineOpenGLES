package good.damn.engine.opengl.shaders

import android.opengl.GLES20
import android.opengl.GLES30.*
import android.util.Log
import androidx.annotation.CallSuper
import good.damn.engine.utils.MGUtilsShader

abstract class MGShaderBase
: MGIShader {
    companion object {
        private const val TAG = "MGShaderBase"

        const val INDEX_ATTRIB_POSITION = 0
        const val INDEX_ATTRIB_TEXTURE_COORDS = 1
        const val INDEX_ATTRIB_NORMALS = 2
        const val INDEX_ATTRIB_INSTANCE_MODEL = 3
        const val INDEX_ATTRIB_INSTANCE_ROTATION = 7
    }

    private var mProgram = 0

    override fun use() {
        glUseProgram(
            mProgram
        )
    }

    fun compile(
        pathVertex: String,
        pathFragment: String
    ): Int {
        mProgram = MGUtilsShader.createProgramFromAssets(
            pathVertex,
            pathFragment
        )
        bindAttribLocation(
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
        pathFragment: String
    ) {
        compile(
            pathVertex,
            pathFragment
        )
        link()
        setupUniforms(
            mProgram
        )
    }

    private inline fun bindAttribLocation(
        program: Int
    ) {
        glBindAttribLocation(
            program,
            INDEX_ATTRIB_POSITION,
            "position"
        )

        glBindAttribLocation(
            program,
            INDEX_ATTRIB_TEXTURE_COORDS,
            "texCoord"
        )

        glBindAttribLocation(
            program,
            INDEX_ATTRIB_NORMALS,
            "normal"
        )

        glBindAttribLocation(
            program,
            INDEX_ATTRIB_INSTANCE_MODEL,
            "modelInstance"
        )

        glBindAttribLocation(
            program,
            INDEX_ATTRIB_INSTANCE_ROTATION,
            "instanceRotation"
        )
    }
}