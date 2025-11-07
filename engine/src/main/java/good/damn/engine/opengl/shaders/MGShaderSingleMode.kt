package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetAttribLocation
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUseProgram
import android.util.Log
import androidx.annotation.CallSuper

open class MGShaderSingleMode
: MGShaderSingleModeInstanced(),
MGIShaderModel {

    final override var uniformModelView = 0
        private set

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        super.setupUniforms(
            program
        )

        // Uniforms
        uniformModelView = glGetUniformLocation(
            program,
            "model"
        )
    }
}