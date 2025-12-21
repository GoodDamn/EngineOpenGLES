package good.damn.engine.opengl.models

import android.util.Pair
import good.damn.engine.sdk.SDVector3
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel

data class MGMPoolMesh(
    val vertexArray: MGArrayVertexManager,
    val pointMinMax: Pair<SDVector3, SDVector3>,
    val material: MGMaterial,
    val pointMiddle: SDVector3,
    val shader: MGShaderGeometryPassModel
)