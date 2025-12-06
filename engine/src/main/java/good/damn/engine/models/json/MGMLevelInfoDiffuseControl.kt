package good.damn.engine.models.json

import org.json.JSONObject

data class MGMLevelInfoDiffuseControl(
    val diffuseMapName: String,
    val controlMapName: String
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelInfoDiffuseControl(
            json.getString(
                "diffuseMap"
            ),
            json.getString(
                "control"
            )
        )
    }
}