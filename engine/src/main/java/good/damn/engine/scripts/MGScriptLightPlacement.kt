package good.damn.engine.scripts

import dalvik.system.DexClassLoader
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.light.MGDrawerLightPoint
import good.damn.engine.opengl.drawers.volume.MGVolumeLight
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerVolume
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateableLight
import good.damn.engine.runnables.MGManagerProcessTime
import good.damn.engine.sdk.models.SDMLightPointEntity
import good.damn.engine.sdk.process.SDIProcessTime
import java.io.File

class MGScriptLightPlacement(
    private val dirScripts: File,
    private val managerProcessTime: MGManagerProcessTime,
    private val managerLight: MGManagerLight,
    private val managerLightVolume: MGManagerVolume
): MGIScript {

    override fun execute() {
        try {
            val loader = DexClassLoader(
                "$dirScripts/SDSceneLight.jar",
                dirScripts.path,
                null,
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

                    val lightPoint = MGDrawerLightPoint(
                        this
                    )

                    managerLight.register(
                        lightPoint
                    )

                    managerLightVolume.addVolume(
                        MGVolumeLight(
                            lightPoint,
                            MGDrawerPositionEntity(
                                modelMatrix.matrixTrigger.model
                            )
                        )
                    )

                }
            }
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }
}