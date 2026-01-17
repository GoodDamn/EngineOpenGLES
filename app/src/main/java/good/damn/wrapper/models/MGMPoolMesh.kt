package good.damn.wrapper.models

import good.damn.apigl.drawers.MGDrawerVertexArray
import good.damn.logic.models.LGTriggerPoint

data class MGMPoolMesh(
    val drawerVertexArray: good.damn.apigl.drawers.MGDrawerVertexArray,
    val triggerPoint: LGTriggerPoint
)