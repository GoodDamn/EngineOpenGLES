package good.damn.engine.models.json

import org.json.JSONObject

data class MGMLevelInfoLandscape(
    val a3dMesh: String,
    val textures: MGMLevelInfoTexturesLandscape
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelInfoLandscape(
            json.getString(
                "file"
            ),
            MGMLevelInfoTexturesLandscape.createFromJson(
                json.getJSONArray(
                    "textures"
                )
            )
        )
    }
}