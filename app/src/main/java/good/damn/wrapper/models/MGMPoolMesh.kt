package good.damn.wrapper.models

import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.logic.models.LGTriggerPoint

data class MGMPoolMesh(
    val drawerVertexArray: MGDrawerVertexArray,
    val triggerPoint: LGTriggerPoint
)