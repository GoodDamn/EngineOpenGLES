package good.damn.engine.opengl.models

import android.util.Pair
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.entities.MGMaterial
import java.nio.IntBuffer

data class MGMPoolMesh(
    val vertexArray: MGArrayVertexManager,
    val pointMinMax: Pair<MGVector, MGVector>,
    val material: MGMaterial,
    val pointMiddle: MGVector
)