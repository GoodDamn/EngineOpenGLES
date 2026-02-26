package good.damn.engine2.shader

import good.damn.common.utils.COUtilsFile
import good.damn.engine2.models.MGMShaderSourceFragDefer
import good.damn.apigl.utils.GLUtilsShader

class MGShaderSource(
    localPath: String,
    buffer: ByteArray
) {
    val fragDefer1: String
    val fragDeferMain: String

    val fragDeferSpecular: MGMShaderSourceFragDefer
    val fragDeferDiffuse: MGMShaderSourceFragDefer
    val fragDeferOpacity: MGMShaderSourceFragDefer
    val fragDeferEmissive: MGMShaderSourceFragDefer
    val fragDeferNormal: MGMShaderSourceFragDefer
    val fragDeferDepth: MGMShaderSourceFragDefer

    val verti: String
    val vert: String

    init {
        val localFullPath = "shaders/$localPath"
        val deferPath = "$localFullPath/defer"
        fragDefer1 = loadSourceCode(
            "$deferPath/frag1.glsl",
            buffer
        )

        fragDeferMain = loadSourceCode(
            "$deferPath/frag_defer.glsl",
            buffer
        )

        fragDeferSpecular = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_spec.glsl",
                buffer
            ),
            loadSourceCode(
                "$deferPath/frag_defer_spec_no.glsl",
                buffer
            ),
            "textMetallic",
            "_m.jpg"
        )

        fragDeferDiffuse = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_diffuse.glsl",
                buffer
            ),
            loadSourceCode(
                "$deferPath/frag_defer_diffuse_no.glsl",
                buffer
            ),
            "textDiffuse",
            ".jpg"
        )

        fragDeferOpacity = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_opacity.glsl",
                buffer
            ),
            loadSourceCode(
                "$deferPath/frag_defer_opacity_no.glsl",
                buffer
            ),
            "textOpacity",
            "_o.jpg"
        )

        fragDeferEmissive = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_emissive.glsl",
                buffer
            ),
            loadSourceCode(
                "$deferPath/frag_defer_emissive_no.glsl",
                buffer
            ),
            "textEmissive",
            "_e.jpg"
        )

        fragDeferNormal = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_normal.glsl",
                buffer
            ),
            loadSourceCode(
                "$deferPath/frag_normal_no.glsl",
                buffer
            ),
            "textNormal",
            "_n.jpg"
        )

        fragDeferDepth = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_depth_func.glsl",
                buffer
            ),
            loadSourceCode(
                "$deferPath/frag_defer_depth_const.glsl",
                buffer
            ),
            "",
            ""
        )

        verti = loadSourceCode(
            "$localFullPath/vert_i.glsl",
            buffer
        )

        vert = loadSourceCode(
            "$localFullPath/vert.glsl",
            buffer
        )
    }

    private inline fun loadSourceCode(
        localFullPath: String,
        buffer: ByteArray
    ) = GLUtilsShader.loadString(
        COUtilsFile.getPublicFile(
            localFullPath
        ),
        buffer
    )
}