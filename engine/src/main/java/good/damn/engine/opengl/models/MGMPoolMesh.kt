package good.damn.engine.opengl.models

import android.util.Pair
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitch
import good.damn.engine.opengl.drawers.MGDrawerModeSwitch

data class MGMPoolMesh(
    val vertexArray: MGArrayVertex,
    val pointMinMax: Pair<MGVector, MGVector>,
    val drawerMode: MGDrawerModeSwitch,
    val pointMiddle: MGVector
)