package good.damn.engine.opengl.shaders.lightpass

import android.opengl.GLES30
import android.opengl.GLES30.glGetUniformBlockIndex
import android.opengl.GLES30.glUniformBlockBinding
import good.damn.engine.opengl.shaders.MGIShaderCameraPosition
import good.damn.engine.opengl.shaders.MGIShaderLight
import good.damn.engine.opengl.shaders.MGShaderLightDirectional
import good.damn.engine.opengl.shaders.MGShaderProjectionView
import good.damn.engine.opengl.shaders.MGShaderTexture
import java.util.LinkedList

class MGShaderLightPass private constructor(
    val textures: Array<MGShaderTexture>
): MGShaderProjectionView(),
    MGIShaderCameraPosition,
    MGIShaderLight {

    override var uniformCameraPosition = 0
        private set

    override val lightDirectional = MGShaderLightDirectional()

    override fun setupUniforms(
        program: Int
    ) {
        super.setupUniforms(
            program
        )

        lightDirectional.setupUniforms(
            program
        )

        textures.forEach {
            it.setupUniforms(
                program
            )
        }

        uniformCameraPosition = GLES30.glGetUniformLocation(
            program,
            "cameraPosition"
        )
    }


    class Builder {
        private val list = LinkedList<
            MGShaderTexture
        >()

        fun attachPosition() = attachTexture(
            "gPosition"
        )

        fun attachNormal() = attachTexture(
            "gNormal"
        )

        fun attachColorSpec() = attachTexture(
            "gColorSpec"
        )

        fun attachMisc() = attachTexture(
            "gMisc"
        )

        fun attachDepth() = attachTexture(
            "gDepth"
        )

        inline fun attachAll() = attachPosition()
            .attachNormal()
            .attachColorSpec()
            .attachMisc()
            .attachDepth()

        fun build() = MGShaderLightPass(
            list.toTypedArray()
        )

        private inline fun attachTexture(
            name: String
        ) = apply {
            list.add(
                MGShaderTexture(
                    name
                )
            )
        }
    }
}