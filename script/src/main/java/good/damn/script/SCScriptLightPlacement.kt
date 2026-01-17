package good.damn.script

import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint
import good.damn.engine.opengl.drawers.volume.MGVolumeLight
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import good.damn.engine.runnables.MGManagerProcessTime
import good.damn.engine.sdk.models.SDMLightPointEntity
import good.damn.engine.sdk.process.SDIProcessTime
import java.io.File

class SCScriptLightPlacement(
    private val dirScripts: File,
    private val managerProcessTime: MGManagerProcessTime,
    private val managerLight: MGManagerLight,
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
                MGDrawerTriggerStateableLight.createFromLight(
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

                    val drawerLightPoint = MGDrawerLightPoint(
                        this
                    )

                    managerLight.register(
                        drawerLightPoint
                    )

                    managerLightVolume.volumes.add(
                        MGVolumeLight(
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