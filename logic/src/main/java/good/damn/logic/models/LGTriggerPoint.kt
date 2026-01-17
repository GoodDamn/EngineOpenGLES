package good.damn.logic.models

import android.util.Pair
import good.damn.engine.sdk.SDVector3

data class LGTriggerPoint(
    val pointMinMax: Pair<SDVector3, SDVector3>,
    val pointMiddle: SDVector3
)