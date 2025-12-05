package good.damn.engine.models.json

import org.json.JSONArray
import org.json.JSONObject

data class MGMLevelInfoTextures(
    val textures: Array<MGMLevelInfoDiffuse>
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONArray
        ) = MGMLevelInfoTextures(
            Array(json.length()) {
                MGMLevelInfoDiffuse.createFromJson(
                    json.getJSONObject(it)
                )
            }
        )
    }
}