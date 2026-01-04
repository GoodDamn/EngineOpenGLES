package good.damn.engine.models.json.light

import android.graphics.Color
import androidx.annotation.ColorInt
import good.damn.engine.sdk.models.SDMLightPointInterpolation
import org.json.JSONObject

data class MGMLight(
    @ColorInt val color: Int,
    val interpolation: SDMLightPointInterpolation
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLight(
            Color.parseColor(
                json.getString(
                    "color"
                )
            ),
            SDMLightPointInterpolation(
                json.getFloat("c"),
                json.getFloat("l"),
                1.0f,
                json.getFloat("r")
            )
        )

        private inline fun JSONObject.getFloat(
            name: String
        ) = getDouble(
            name
        ).toFloat()
    }
}