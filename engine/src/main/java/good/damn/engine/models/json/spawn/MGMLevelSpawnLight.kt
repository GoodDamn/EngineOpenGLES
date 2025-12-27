package good.damn.engine.models.json.spawn

import android.graphics.Color
import androidx.annotation.ColorInt
import org.json.JSONObject

data class MGMLevelSpawnLight(
    @ColorInt val color: Int
) {
    companion object {
        @JvmStatic
        fun createFromJson(
            json: JSONObject
        ) = MGMLevelSpawnLight(
            Color.parseColor(
                json.getString(
                    "color"
                )
            )
        )
    }
}