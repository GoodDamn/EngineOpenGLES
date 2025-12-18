package good.damn.engine.opengl.shaders

import android.opengl.GLES30.glGetUniformBlockIndex
import android.opengl.GLES30.glGetUniformLocation
import android.opengl.GLES30.glUniformBlockBinding
import androidx.annotation.CallSuper
import good.damn.engine.opengl.shaders.base.MGShaderBase

open class MGShaderOpaque(
    override val materials: Array<MGShaderMaterial>
): MGShaderBase(),
MGIShaderNormal,
MGIShaderCameraPosition,
MGIShaderLight,
MGIShaderMaterial {

    companion object {
        private const val NUM_LIGHTS = 4
    }

    final override var uniformCameraPosition = 0
        private set

    final override var uniformNormalMatrix = 0
        private set

    final override val lightDirectional = MGShaderLightDirectional()

    final override val lightPoints = Array(
        NUM_LIGHTS
    ) { MGShaderLightPoint(it) }

    @CallSuper
    override fun setupUniforms(
        program: Int
    ) {
        lightDirectional.setupUniforms(
            program
        )

        lightPoints.forEach {
            it.setupUniforms(
                program
            )
        }

        materials.forEach {
            it.setupUniforms(
                program
            )
        }

        // Uniforms
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