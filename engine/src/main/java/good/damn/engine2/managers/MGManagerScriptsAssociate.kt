package good.damn.engine2.managers

import good.damn.apigl.drawers.GLDrawerLightPoint
import good.damn.apigl.drawers.GLDrawerLights
import good.damn.apigl.drawers.GLVolumeLight
import good.damn.common.volume.COIVolume
import good.damn.engine.sdk.managers.SDManagerLights
import good.damn.engine.sdk.managers.SDManagerProcessTime
import good.damn.engine.sdk.models.SDMLightPointEntity
import good.damn.engine.sdk.models.provider.SDMProvider
import good.damn.engine.sdk.models.provider.SDMProviderComponents
import good.damn.engine.sdk.models.provider.SDMProviderManagers
import good.damn.engine.sdk.process.SDIProcessTime
import good.damn.engine2.providers.MGProviderGL
import good.damn.logic.triggers.stateables.LGTriggerStateableLight
import good.damn.script.SCManagerScripts
import java.util.LinkedList
import java.util.concurrent.ConcurrentLinkedQueue

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
                SDMProviderManagers(
                    SDManagerProcessTime(
                        runnablesLoop
                    ),
                    SDManagerLights(
                        lights
                    )
                ),
                SDMProviderComponents(
                    glProvider.drawers.drawerLightDirectional.info
                )
            )
        )


        associateProvider(
            runnablesLoop,
            lights
        )
    }


    private inline fun associateProvider(
        runnablesLoop: LinkedList<SDIProcessTime>,
        lights: LinkedList<SDMLightPointEntity>,
    ) = glProvider.apply {

        runnablesLoop.apply {
            if (isNotEmpty()) {
                managers.managerProcessTime.registerLoopProcessTime(
                    runnablesLoop
                )
            }
        }

        val volumes = ConcurrentLinkedQueue<
            COIVolume
        >()

        lights.apply {
            if (isEmpty()) {
                return@apply
            }

            val listLights = LinkedList<
                GLDrawerLightPoint
            >()

            forEach {
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

                    listLights.add(
                        drawerLightPoint
                    )

                    volumes.add(
                        GLVolumeLight(
                            drawerLightPoint,
                            modelMatrix.matrixTrigger.model
                        )
                    )
                }
            }

            managers.managerLight.lights.put(
                listLights.hashCode(),
                listLights
            )
        }

        volumes.apply {
            if (isNotEmpty()) {
                managers.managerFrustrum.volumes[
                    volumes.hashCode()
                ] = volumes
            }
        }

    }

}