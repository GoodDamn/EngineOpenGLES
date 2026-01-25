package good.damn.script

import good.damn.apigl.drawers.GLDrawerLightPoint
import good.damn.apigl.drawers.GLDrawerLights
import good.damn.apigl.drawers.GLVolumeLight
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.sdk.models.SDMLightPointEntity
import good.damn.engine.sdk.process.SDIProcessTime
import good.damn.logic.process.LGManagerProcessTime
import good.damn.logic.triggers.stateables.LGTriggerStateableLight
import java.io.File

class SCScriptLightPlacement(
    private val dirScripts: File,
    private val managerProcessTime: LGManagerProcessTime,
    private val managerLight: GLDrawerLights,
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

                    managerLight.register(
                        drawerLightPoint
                    )

                    managerLightVolume.volumes.add(
                        GLVolumeLight(
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