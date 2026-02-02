package good.damn.engine2.models.json

import org.json.JSONObject

data class MGMLevelInfoTexture(
    val diffuseMapName: String,
    val controlMapName: String?
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelInfoTexture(
            json.getString(
                "diffuseMap"
            ),
            try {
                json.getString(
                    "control"
                )
            } catch (e: Exception) {
                null
            }
        )
    }
}