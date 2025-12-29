package good.damn.engine.scripts

import dalvik.system.DexClassLoader
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.runnables.MGManagerProcessTime
import good.damn.engine.sdk.models.SDMLightPointEntity
import good.damn.engine.sdk.process.SDIProcessTime
import java.io.File

class MGScriptLightPlacement(
    private val dirScripts: File,
    private val managerProcessTime: MGManagerProcessTime,
    private val managerTriggerLight: MGManagerLight
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
                MGTriggerLight.createFromLight(
                    it.light
                ).run {
                    matrix.radius = it.radiusTrigger
                    matrix.setPosition(
                        it.position.x,
                        it.position.y,
                        it.position.z
                    )
                    matrix.invalidatePosition()
                    matrix.invalidateRadius()
                    matrix.calculateInvertTrigger()

                    managerTriggerLight.register(
                        triggerState
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