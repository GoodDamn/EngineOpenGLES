package good.damn.apigl.shaders.lightpass

import android.opengl.GLES30
import good.damn.apigl.shaders.GLShaderLightPoint
import java.util.LinkedList

class GLShaderLightPassPointLight private constructor(
    val textures: Array<good.damn.apigl.shaders.GLShaderTexture>
): good.damn.apigl.shaders.GLShaderProjectionViewModel(),
    good.damn.apigl.shaders.GLIShaderCameraPosition {

    override var uniformCameraPosition = 0
        private set

    val lightPoint = GLShaderLightPoint()
    val lightDirectional = good.damn.apigl.shaders.GLShaderLightDirectional()

    var uniformScreenSize = 0
        private set

    override fun setupUniforms(
        program: Int
    ) {
        super.setupUniforms(
            program
        )

        uniformScreenSize = GLES30.glGetUniformLocation(
            program,
            "uScreenSize"
        )

        lightPoint.setupUniforms(
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
            good.damn.apigl.shaders.GLShaderTexture
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

        fun build() = GLShaderLightPassPointLight(
            list.toTypedArray()
        )

        private inline fun attachTexture(
            name: String
        ) = apply {
            list.add(
                good.damn.apigl.shaders.GLShaderTexture(
                    name
                )
            )
        }
    }
}