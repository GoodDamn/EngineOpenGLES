package good.damn.engine.opengl.models

import android.util.Pair
import good.damn.engine.sdk.SDVector3
import good.damn.engine.opengl.drawers.MGDrawerVertexArray

data class MGMPoolVertexArray(
    val drawerVertexArray: MGDrawerVertexArray,
    val pointMinMax: Pair<SDVector3, SDVector3>,
    val pointMiddle: SDVector3
)