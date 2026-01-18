package good.damn.engine2.models.json

import org.json.JSONObject

data class MGMLevelInfoMesh(
    val a3dMesh: String,
    val ambientOcclusionMapName: String?,
    val textures: MGMLevelInfoTextures
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelInfoMesh(
            json.getString(
                "file"
            ),
            try {
                json.getString(
                    "ao"
                )
            } catch (e: Exception) {
                null
            },
            MGMLevelInfoTextures.createFromJson(
                json.getJSONArray(
                    "textures"
                )
            )
        )
    }
}