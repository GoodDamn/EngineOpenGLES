package good.damn.engine.scripts

import android.util.Log
import dalvik.system.DexClassLoader
import good.damn.engine.MGEngine
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
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

            val c = loader.loadClass("engine.MGLightDir")
            val method = c.getMethod(
                "createColorAmbient"
            )

            val colorAmbient = method.invoke(
                c.newInstance()
            ) as? ByteArray

            directionalLight.ambColor.run {
                x = ((colorAmbient?.get(0)?.toInt() ?: 0) and 0xff) / 255f
                y = ((colorAmbient?.get(1)?.toInt() ?: 0) and 0xff) / 255f
                z = ((colorAmbient?.get(2)?.toInt() ?: 0) and 0xff) / 255f
            }
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }

}