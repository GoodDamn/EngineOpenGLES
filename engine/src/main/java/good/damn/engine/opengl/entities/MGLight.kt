package good.damn.engine.opengl.entities

import good.damn.engine.opengl.MGVector

data class MGLight(
    var color: MGVector,
    var position: MGVector,
    val radius: Float
)