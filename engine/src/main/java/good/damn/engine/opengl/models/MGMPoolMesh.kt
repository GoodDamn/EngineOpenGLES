package good.damn.engine.opengl.models

import android.util.Pair
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.entities.MGMaterial

data class MGMPoolMesh(
    val vertexArray: MGArrayVertex,
    val pointMinMax: Pair<MGVector, MGVector>,
    val material: MGMaterial,
    val pointMiddle: MGVector
)