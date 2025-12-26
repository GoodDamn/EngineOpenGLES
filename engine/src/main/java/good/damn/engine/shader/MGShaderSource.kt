package good.damn.engine.shader

import good.damn.engine.opengl.models.MGMShader
import good.damn.engine.opengl.models.MGMShaderSourceFragDefer
import good.damn.engine.utils.MGUtilsAsset
import good.damn.engine.utils.MGUtilsFile

class MGShaderSource(
    localPath: String,
) {
    companion object {
        private const val FOLDER = "shaders"
    }

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
        val localFullPath = "$FOLDER/$localPath"
        val deferPath = "$localFullPath/defer"
        fragDefer1 = loadSourceCode(
            "$deferPath/frag1.glsl"
        )

        fragDeferMain = loadSourceCode(
            "$deferPath/frag_defer.glsl"
        )

        fragDeferSpecular = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_spec.glsl"
            ),
            loadSourceCode(
                "$deferPath/frag_defer_spec_no.glsl"
            ),
            "textMetallic",
            "_m.jpg"
        )

        fragDeferDiffuse = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_diffuse.glsl"
            ),
            loadSourceCode(
                "$deferPath/frag_defer_diffuse_no.glsl"
            ),
            "textDiffuse",
            ".jpg"
        )

        fragDeferOpacity = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_opacity.glsl"
            ),
            loadSourceCode(
                "$deferPath/frag_defer_opacity_no.glsl"
            ),
            "textOpacity",
            "_o.jpg"
        )

        fragDeferEmissive = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_emissive.glsl"
            ),
            loadSourceCode(
                "$deferPath/frag_defer_emissive_no.glsl"
            ),
            "textEmissive",
            "_e.jpg"
        )

        fragDeferNormal = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_normal.glsl"
            ),
            loadSourceCode(
                "$deferPath/frag_normal_no.glsl"
            ),
            "textNormal",
            "_n.jpg"
        )

        fragDeferDepth = MGMShaderSourceFragDefer(
            loadSourceCode(
                "$deferPath/frag_defer_depth_func.glsl"
            ),
            loadSourceCode(
                "$deferPath/frag_defer_depth_const.glsl"
            ),
            "",
            ""
        )

        verti = loadSourceCode(
            "$localFullPath/vert_i.glsl"
        )

        vert = loadSourceCode(
            "$localFullPath/vert.glsl"
        )
    }

    private inline fun loadSourceCode(
        localFullPath: String
    ) = MGUtilsAsset.loadString(
        MGUtilsFile.getPublicFile(
            localFullPath
        )
    )
}