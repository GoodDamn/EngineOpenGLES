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
    val frag2: String

    val fragNormalNo: String
    val fragNormalMap: String

    val fragMetallicNo: String
    val fragMetallicMap: String

    val fragSpecularNo: String
    val fragSpecular: String

    val fragOpacityNo: String
    val fragOpacityMap: String

    val fragEmissiveNo: String
    val fragEmissiveMap: String


    val fragLight: String

    val verti: String
    val vert: String

    init {
        val localFullPath = "$FOLDER/$localPath"
        frag1 = loadSourceCode(
            "$localFullPath/frag1.glsl"
        )

        frag2 = loadSourceCode(
            "$localFullPath/frag2.glsl"
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



        fragMetallicMap = MGUtilsAsset.loadString(
            "$localFullPath/frag_metal.glsl"
        )

        fragMetallicNo = MGUtilsAsset.loadString(
            "$localFullPath/frag_metal_no.glsl"
        )



        fragEmissiveMap = MGUtilsAsset.loadString(
            "$localFullPath/frag_emissive.glsl"
        )

        fragEmissiveNo = MGUtilsAsset.loadString(
            "$localFullPath/frag_emissive_no.glsl"
        )



        fragOpacityMap = MGUtilsAsset.loadString(
            "$localFullPath/frag_opacity.glsl"
        )

        fragOpacityNo = MGUtilsAsset.loadString(
            "$localFullPath/frag_opacity_no.glsl"
        )

        fragLight = MGUtilsAsset.loadString(
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