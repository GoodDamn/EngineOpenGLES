package good.damn.engine.opengl.models

import android.util.Pair
import good.damn.engine.sdk.MGVector3
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.entities.MGMaterial

data class MGMPoolMesh(
    val vertexArray: MGArrayVertexManager,
    val pointMinMax: Pair<MGVector3, MGVector3>,
    val material: MGMaterial,
    val pointMiddle: MGVector3
)