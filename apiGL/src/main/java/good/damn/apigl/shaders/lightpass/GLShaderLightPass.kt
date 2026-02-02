package good.damn.apigl.shaders.lightpass

import android.opengl.GLES30
import good.damn.apigl.shaders.GLIShaderCameraPosition
import good.damn.apigl.shaders.GLIShaderLight
import good.damn.apigl.shaders.GLShaderLightDirectional
import good.damn.apigl.shaders.GLShaderProjectionView
import good.damn.apigl.shaders.GLShaderTexture
import java.util.LinkedList

class GLShaderLightPass private constructor(
    val textures: Array<GLShaderTexture>
): GLShaderProjectionView(),
GLIShaderCameraPosition,
GLIShaderLight {

    override var uniformCameraPosition = 0
        private set

    override val lightDirectional = GLShaderLightDirectional()

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
            GLShaderTexture
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

        fun attachAll() = attachPosition()
            .attachNormal()
            .attachColorSpec()
            .attachMisc()
            .attachDepth()

        fun build() = GLShaderLightPass(
            list.toTypedArray()
        )

        private inline fun attachTexture(
            name: String
        ) = apply {
            list.add(
                GLShaderTexture(
                    name
                )
            )
        }
    }
}