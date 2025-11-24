package good.damn.engine.scripts

import android.util.Log
import dalvik.system.DexClassLoader
import good.damn.engine.MGEngine
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.sdk.MGVector3
import good.damn.engine.utils.MGUtilsFile
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
                "createColorAmbient"
            )

            val colorAmbient = method.invoke(
                c.newInstance()
            ) as? MGVector3 ?: return

            directionalLight.ambColor.copy(
                colorAmbient
            )
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }

}