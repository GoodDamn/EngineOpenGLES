package good.damn.engine.models.json

import org.json.JSONObject

data class MGMLevelInfoMesh(
    val a3dMesh: String,
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
            MGMLevelInfoTextures.createFromJson(
                json.getJSONArray(
                    "textures"
                )
            )
        )
    }
}