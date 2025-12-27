package good.damn.engine.models.json.spawn

import org.json.JSONObject

data class MGMLevelSpawnInfo(
    val mesh: String,
    val rotX: Float,
    val positionYDt: Float,
    val info: Array<MGMLevelSpawnType>
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelSpawnInfo(
            json.getString(
                "mesh"
            ),
            json.getDouble(
                "rotX"
            ).toFloat(),
            json.getDouble(
                "positionYDt"
            ).toFloat(),
            json.getJSONArray(
                "pointTypes"
            ).run {
                Array(length()) {
                    MGMLevelSpawnType.createFromJson(
                        getJSONObject(it)
                    )
                }
            }
        )
    }
}