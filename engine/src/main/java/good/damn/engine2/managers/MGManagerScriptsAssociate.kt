package good.damn.engine2.managers

import good.damn.apigl.drawers.GLDrawerLightPoint
import good.damn.apigl.drawers.GLVolumeLight
import good.damn.engine.sdk.managers.SDManagerLights
import good.damn.engine.sdk.managers.SDManagerProcessTime
import good.damn.engine.sdk.models.SDMLightPointEntity
import good.damn.engine.sdk.models.provider.SDMProvider
import good.damn.engine.sdk.process.SDIProcessTime
import good.damn.engine2.providers.MGProviderGL
import good.damn.logic.triggers.stateables.LGTriggerStateableLight
import good.damn.script.SCManagerScripts
import java.util.LinkedList

class MGManagerScriptsAssociate(
    private val managerScripts: SCManagerScripts
): MGProviderGL() {

    fun load(
        scriptName: String
    ) {
        val runnablesLoop = LinkedList<
            SDIProcessTime
        >()

        val lights = LinkedList<
            SDMLightPointEntity
        >()

        managerScripts.load(
            scriptName,
            SDMProvider(
                SDManagerProcessTime(
                    runnablesLoop
                ),
                SDManagerLights(
                    lights
                )
            )
        )

        associateProvider(
            runnablesLoop,
            lights
        )

        runnablesLoop.clear()
        lights.clear()
    }


    private inline fun associateProvider(
        runnablesLoop: LinkedList<SDIProcessTime>,
        lights: LinkedList<SDMLightPointEntity>
    ) = glProvider.apply {
        runnablesLoop.forEach {
            managers.managerProcessTime.registerLoopProcessTime(
                it
            )
        }

        lights.forEach {
            LGTriggerStateableLight.createFromLight(
                it.light
            ).apply {
                modelMatrix.setPosition(
                    it.position.x,
                    it.position.y,
                    it.position.z
                )
                modelMatrix.radius = it.light.interpolation.radius
                modelMatrix.invalidatePosition()
                modelMatrix.invalidateRadius()
                modelMatrix.calculateInvertTrigger()

                val drawerLightPoint = GLDrawerLightPoint(
                    modelMatrix.matrixTrigger.model,
                    it.light
                )

                managers.managerLight.lights.add(
                    drawerLightPoint
                )

                managers.managerFrustrum.volumes.add(
                    GLVolumeLight(
                        drawerLightPoint,
                        modelMatrix.matrixTrigger.model
                    )
                )
            }
        }

    }

}