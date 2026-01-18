package good.damn.engine.opengl.pools

import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.logic.models.LGTriggerPoint

data class MGMPoolMesh(
    val drawerVertexArray: GLDrawerVertexArray,
    val triggerPoint: LGTriggerPoint
)