package good.damn.script

import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.sdk.models.SDMLight
import java.io.File

class SCScriptDirLight(
    private val dirScripts: File,
    private val directionalLight: MGDrawerLightDirectional
): SCIScript {
    companion object {
        private const val SCRIPT_FILE = "MGLightDir.jar"
    }

    override fun execute() {
        try {
            val loader = SCScriptLoaderExternal(
                dirScripts,
                SCRIPT_FILE,
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

            loader.removeScriptFromCache()
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }

}