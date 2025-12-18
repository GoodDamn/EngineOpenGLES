package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformBlockIndex
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUniformBlockBinding
import androidx.annotation.CallSuper
import good.damn.engine.opengl.shaders.base.MGShaderBase

class MGShaderOpaqueDefer(
    override val materials: Array<MGShaderMaterial>
): MGShaderBase(),
MGIShaderNormal,
MGIShaderCameraPosition,
MGIShaderMaterial {

    final override var uniformCameraPosition = 0
        private set

    final override var uniformNormalMatrix = 0
        private set

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        materials.forEach {
            it.setupUniforms(
                program
            )
        }

        glUniformBlockBinding(
            program,
            glGetUniformBlockIndex(
                program,
                "Camera"
            ),
            0
        )

        uniformNormalMatrix = glGetUniformLocation(
            program,
            "normalMatrix"
        )

        uniformCameraPosition = glGetUniformLocation(
            program,
            "cameraPosition"
        )
    }
}