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
    val verti: String

    init {
        val localFullPath = "$FOLDER/$localPath"
        frag1 = MGUtilsAsset.loadString(
            MGUtilsFile.getPublicFile(
                "$localFullPath/frag1.glsl"
            )
        )

        frag2 = MGUtilsAsset.loadString(
            MGUtilsFile.getPublicFile(
                "$localFullPath/frag2.glsl"
            )
        )

        fragNormalNo = MGUtilsAsset.loadString(
            MGUtilsFile.getPublicFile(
                "$localFullPath/frag_normal_no.glsl"
            )
        )

        fragNormalMap = MGUtilsAsset.loadString(
            MGUtilsFile.getPublicFile(
                "$localFullPath/frag_normal.glsl"
            )
        )

        verti = MGUtilsAsset.loadString(
            MGUtilsFile.getPublicFile(
                "$localFullPath/vert_i.glsl"
            )
        )
    }
}