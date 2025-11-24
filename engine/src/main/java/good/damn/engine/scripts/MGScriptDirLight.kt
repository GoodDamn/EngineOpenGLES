package good.damn.engine.scripts

import dalvik.system.DexClassLoader
import good.damn.engine.MGEngine
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.sdk.MGVector3
import good.damn.engine.sdk.models.SDMLight
import java.io.File

class MGScriptDirLight(
    private val dirScripts: File,
    private val directionalLight: MGDrawerLightDirectional
): MGIScript {



    override fun execute() {
        try {
            val loader = DexClassLoader(
                "$dirScripts/MGLightDir.jar",
                MGEngine.DIR_DATA.path,
                null,
                javaClass.classLoader
            )

            val c = loader.loadClass("sdk.engine.MGLightDir")
            val method = c.getMethod(
                "processAmbColor",
                SDMLight::class.java
            )

            method.invoke(
                c.newInstance(),
                directionalLight.info
            )
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }

}