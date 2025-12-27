package good.damn.engine.models.json.spawn

import good.damn.engine.sdk.models.SDMLightPointInterpolation
import org.json.JSONObject

data class MGMLevelSpawnInfo(
    val mesh: String,
    val rotX: Float,
    val positionYDt: Float,
    val lightConstant: Float,
    val lightLinear: Float,
    val lightRadius: Float,
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
            json.getFloat(
                "rotX"
            ),
            json.getFloat(
                "positionYDt"
            ),
            json.getFloat(
                "lightC"
            ),
            json.getFloat(
                "lightL"
            ),
            json.getFloat(
                "radius"
            ),
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

        private inline fun JSONObject.getFloat(
            key: String
        ) = getDouble(
            key
        ).toFloat()
    }
}