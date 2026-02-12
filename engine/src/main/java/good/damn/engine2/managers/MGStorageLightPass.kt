package good.damn.engine2.managers

import good.damn.apigl.enums.GLEnumLightPass
import good.damn.apigl.framebuffer.GLFrameBufferG
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.apigl.shaders.lightpass.GLShaderLightPass
import good.damn.engine2.files.MGFile
import good.damn.engine2.opengl.models.MGMLightPass

class MGStorageLightPass(
    framebufferG: GLFrameBufferG
) {

    private val mLightPasses = arrayOf(
        MGMLightPass(
            arrayOf(
                framebufferG.textureAttachmentPosition.texture,
                framebufferG.textureAttachmentNormal.texture,
                framebufferG.textureAttachmentColorSpec.texture,
                framebufferG.textureAttachmentMisc.texture,
                framebufferG.textureAttachmentDepth.texture
            ),
            GLShaderLightPass.Builder()
                .attachAll()
                .build()
        ),

        MGMLightPass(
            arrayOf(
                framebufferG.textureAttachmentColorSpec.texture
            ),
            GLShaderLightPass.Builder()
                .attachColorSpec()
                .build()
        ),

        MGMLightPass(
            arrayOf(
                framebufferG.textureAttachmentDepth.texture
            ),
            GLShaderLightPass.Builder()
                .attachDepth()
                .build()
        ),

        MGMLightPass(
            arrayOf(
                framebufferG.textureAttachmentNormal.texture
            ),
            GLShaderLightPass.Builder()
                .attachNormal()
                .build()
        )
    )

    fun glSetupShaders(
        buffer: ByteArray,
        binderPositionTexCoords: GLBinderAttribute
    ) {
        val fileVertex = MGFile(
            "shaders/lightPass/vert.glsl"
        )

        mLightPasses[0].shader.glSetupShader(
            buffer,
            fileVertex,
            "shaders/opaque/defer/frag_defer_light_dir.glsl",
            binderPositionTexCoords
        )

        mLightPasses[1].shader.glSetupShader(
            buffer,
            fileVertex,
            "shaders/lightPass/frag_defer.glsl",
            binderPositionTexCoords
        )

        mLightPasses[2].shader.glSetupShader(
            buffer,
            fileVertex,
            "shaders/lightPass/frag_defer_depth.glsl",
            binderPositionTexCoords
        )

        mLightPasses[3].shader.glSetupShader(
            buffer,
            fileVertex,
            "shaders/lightPass/frag_defer_normal.glsl",
            binderPositionTexCoords
        )
    }

    operator fun get(
        type: GLEnumLightPass
    ) = mLightPasses[
        type.ordinal
    ]


    private inline fun GLShaderLightPass.glSetupShader(
        buffer: ByteArray,
        fileVertex: MGFile,
        localPathFragment: String,
        binderPositionTexCoords: GLBinderAttribute
    ) = apply {
        setup(
            buffer,
            fileVertex,
            MGFile(
                localPathFragment
            ),
            binderPositionTexCoords
        )
    }
}