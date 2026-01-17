package good.damn.script

import good.damn.common.volume.COManagerFrustrum
import good.damn.apigl.drawers.MGDrawerLightPoint
import good.damn.apigl.drawers.MGVolumeLight
import good.damn.apigl.drawers.MGDrawerLights
import good.damn.logic.triggers.stateables.MGDrawerTriggerStateableLight
import good.damn.logic.process.MGManagerProcessTime
import good.damn.engine.sdk.models.SDMLightPointEntity
import good.damn.engine.sdk.process.SDIProcessTime
import java.io.File

class SCScriptLightPlacement(
    private val dirScripts: File,
    private val managerProcessTime: good.damn.logic.process.MGManagerProcessTime,
    private val managerLight: good.damn.apigl.drawers.MGDrawerLights,
    private val managerLightVolume: COManagerFrustrum
): SCIScript {

    companion object {
        private const val SCRIPT_FILE = "SDSceneLight.jar"
    }

    override fun execute() {
        try {
            val loader = SCScriptLoaderExternal(
                dirScripts,
                SCRIPT_FILE,
                javaClass.classLoader
            )

            val c = loader.loadClass("sdk.engine.SDSceneLight")
            val methodLightPoints = c.getMethod(
                "getLightPoints"
            )

            val methodProcessTime = c.getMethod(
                "getProcessTimeExecutor"
            )

            val clazz = c.newInstance()

            val lightPoints = methodLightPoints.invoke(
                clazz
            ) as? Array<SDMLightPointEntity>

            (methodProcessTime.invoke(
                clazz
            ) as? SDIProcessTime)?.let {
                managerProcessTime.registerLoopProcessTime(
                    it
                )
            }

            lightPoints?.forEach {
                good.damn.logic.triggers.stateables.MGDrawerTriggerStateableLight.createFromLight(
                    it.light
                ).run {
                    modelMatrix.setPosition(
                        it.position.x,
                        it.position.y,
                        it.position.z
                    )
                    modelMatrix.radius = it.light.interpolation.radius
                    modelMatrix.invalidatePosition()
                    modelMatrix.invalidateRadius()
                    modelMatrix.calculateInvertTrigger()

                    val drawerLightPoint = good.damn.apigl.drawers.MGDrawerLightPoint(
                        this
                    )

                    managerLight.register(
                        drawerLightPoint
                    )

                    managerLightVolume.volumes.add(
                        good.damn.apigl.drawers.MGVolumeLight(
                            drawerLightPoint,
                            modelMatrix.matrixTrigger.model
                        )
                    )
                }
            }

            loader.removeScriptFromCache()
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }
}