package good.damn.apigl.shaders

import android.opengl.GLES30.glGetUniformBlockIndex
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUniformBlockBinding
import androidx.annotation.CallSuper
import good.damn.engine.opengl.shaders.base.MGShaderBase

open class GLShaderGeometryPassInstanced(
    override val materials: Array<GLShaderMaterial>
): MGShaderBase(),
    GLIShaderNormal,
    GLIShaderCameraPosition,
    GLIShaderMaterial {

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