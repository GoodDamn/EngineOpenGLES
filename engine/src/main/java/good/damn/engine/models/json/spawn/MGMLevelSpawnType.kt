package good.damn.engine.models.json.spawn

import org.json.JSONObject

data class MGMLevelSpawnType(
    val texture: String,
    val light: String
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelSpawnType(
            json.getString(
                "texture"
            ),
            json.getString(
                "light"
            )
        )
    }
}