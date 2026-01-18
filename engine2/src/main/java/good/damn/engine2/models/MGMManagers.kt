package good.damn.engine2.models

import good.damn.apigl.drawers.GLDrawerLights
import good.damn.common.volume.COManagerFrustrum
import good.damn.logic.process.LGManagerProcessTime

data class MGMManagers(
    val managerProcessTime: LGManagerProcessTime,
    val managerLight: GLDrawerLights,
    val managerFrustrum: COManagerFrustrum
)