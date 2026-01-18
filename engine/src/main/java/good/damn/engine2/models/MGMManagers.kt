package good.damn.engine2.models

import good.damn.apigl.drawers.GLDrawerLights
import good.damn.common.volume.COManagerFrustrum
import good.damn.logic.process.LGManagerProcessTime
import good.damn.logic.triggers.managers.LGManagerTriggerMesh

data class MGMManagers(
    val managerProcessTime: LGManagerProcessTime,
    val managerLight: GLDrawerLights,
    val managerFrustrum: COManagerFrustrum,
    val managerTrigger: LGManagerTriggerMesh
)