package good.damn.engine.runnables

import good.damn.engine.models.MGMInformator
import good.damn.engine.sdk.process.SDIProcessTime

class MGRunnableTriggerLoop(
    private val informator: MGMInformator
): SDIProcessTime {
    override fun onProcessTime(
        dt: Float
    ) {
        /*informator.managerTriggerLight.loopTriggers(
            informator.camera.modelMatrix.x,
            informator.camera.modelMatrix.y,
            informator.camera.modelMatrix.z
        )*/
    }

}