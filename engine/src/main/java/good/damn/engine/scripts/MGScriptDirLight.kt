package good.damn.engine.scripts

import android.util.Log
import dalvik.system.DexClassLoader
import good.damn.engine.MGEngine
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.extensions.writeToFile
import good.damn.engine.sdk.models.SDMLight
import java.io.File

class MGScriptDirLight(
    private val dirScripts: File,
    private val directionalLight: MGDrawerLightDirectional
): MGIScript {
    companion object {
        private const val SCRIPT_FILE = "MGLightDir.jar"
    }

    override fun execute() {
        try {
            val loader = MGLoaderScriptExternal(
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