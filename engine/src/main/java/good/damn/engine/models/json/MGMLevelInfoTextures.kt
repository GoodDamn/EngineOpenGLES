package good.damn.engine.models.json

import org.json.JSONArray

data class MGMLevelInfoTextures(
    val textures: Array<MGMLevelInfoTexture>
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONArray
        ) = MGMLevelInfoTextures(
            Array(json.length()) {
                MGMLevelInfoTexture.createFromJson(
                    json.getJSONObject(it)
                )
            }
        )
    }
}