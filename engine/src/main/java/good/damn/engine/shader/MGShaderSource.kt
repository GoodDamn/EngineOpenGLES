package good.damn.engine.shader

import good.damn.engine.utils.MGUtilsAsset
import good.damn.engine.utils.MGUtilsFile

class MGShaderSource(
    localPath: String,
) {
    companion object {
        private const val FOLDER = "shaders"
    }

    val frag1: String
    val fragMain: String

    val fragNormalNo: String
    val fragNormalMap: String

    val fragSpecularNo: String
    val fragSpecular: String

    val fragTexture: String
    val fragTextureNo: String

    val fragMaterial: String

    val fragLight: String

    val fragDefer1: String
    val fragDeferMain: String
    val fragDeferSpecular: String
    val fragDeferSpecularNo: String

    val fragDeferDiffuse: String
    val fragDeferDiffuseNo: String

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

        fragDeferSpecular = loadSourceCode(
            "$deferPath/frag_defer_spec.glsl"
        )

        fragDeferSpecularNo = loadSourceCode(
            "$deferPath/frag_defer_spec_no.glsl"
        )

        fragDeferDiffuse = loadSourceCode(
            "$deferPath/frag_defer_diffuse.glsl"
        )

        fragDeferDiffuseNo = loadSourceCode(
            "$deferPath/frag_defer_diffuse_no.glsl"
        )

        frag1 = loadSourceCode(
            "$localFullPath/frag1.glsl"
        )

        fragMain = loadSourceCode(
            "$localFullPath/frag_main.glsl"
        )

        fragNormalNo = loadSourceCode(
            "$localFullPath/frag_normal_no.glsl"
        )

        fragNormalMap = loadSourceCode(
            "$localFullPath/frag_normal.glsl"
        )


        fragSpecular = loadSourceCode(
            "$localFullPath/frag_spec.glsl"
        )

        fragSpecularNo = loadSourceCode(
            "$localFullPath/frag_spec_no.glsl"
        )

        fragTexture = loadSourceCode(
            "$localFullPath/frag_texture.glsl"
        )

        fragTextureNo = loadSourceCode(
            "$localFullPath/frag_texture_no.glsl"
        )


        fragMaterial = loadSourceCode(
            "$localFullPath/frag_material.glsl"
        )


        fragLight = loadSourceCode(
            "$localFullPath/frag_light.glsl"
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