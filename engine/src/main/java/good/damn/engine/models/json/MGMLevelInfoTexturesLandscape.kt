package good.damn.engine.models.json

import org.json.JSONArray

data class MGMLevelInfoTexturesLandscape(
    val textures: Array<MGMLevelInfoDiffuseControl>
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONArray
        ) = MGMLevelInfoTexturesLandscape(
            Array(json.length()) {
                MGMLevelInfoDiffuseControl.createFromJson(
                    json.getJSONObject(it)
                )
            }
        )
    }
}