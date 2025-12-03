package good.damn.engine.models.json

import org.json.JSONObject

data class MGMLevelInfoDiffuse(
    val diffuseMapName: String
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelInfoDiffuse(
            json.getString(
                "diffuseMap"
            )
        )
    }
}