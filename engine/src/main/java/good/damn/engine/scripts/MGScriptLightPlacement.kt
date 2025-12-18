package good.damn.engine.scripts

import dalvik.system.DexClassLoader
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.sdk.models.SDMLight
import good.damn.engine.sdk.models.SDMLightPointEntity
import java.io.File

class MGScriptLightPlacement(
    private val dirScripts: File,
    private val managerTriggerLight: MGManagerTriggerLight
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
            val method = c.getMethod(
                "getLightPoints"
            )

            val clazz = c.newInstance()

            val lightPoints = method.invoke(
                clazz
            ) as? Array<SDMLightPointEntity>

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

                    managerTriggerLight.addTrigger(
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