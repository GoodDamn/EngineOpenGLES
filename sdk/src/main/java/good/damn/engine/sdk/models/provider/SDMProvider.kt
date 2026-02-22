package good.damn.engine.sdk.models.provider

import good.damn.engine.sdk.managers.SDManagerLights
import good.damn.engine.sdk.managers.SDManagerProcessTime

data class SDMProvider(
    val managerProcessTime: SDManagerProcessTime,
    val managerLights: SDManagerLights
)