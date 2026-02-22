package good.damn.script

import good.damn.engine.sdk.managers.SDManagerLights
import good.damn.engine.sdk.managers.SDManagerProcessTime
import good.damn.engine.sdk.models.provider.SDMProvider
import java.io.File
import java.io.IOException
import java.util.LinkedList
import kotlin.jvm.Throws

class SCManagerScripts(
    private val dirScripts: File
) {

    private companion object {
        private const val PACKAGE = "sdk.engine."
        private const val SMETHOD_EXECUTE = "execute"
        private const val SMETHOD_SET_PROVIDER = "setSdProvider"
    }

    @Throws(IOException::class)
    fun load(
        scriptName: String,
        file: File
    ) {
        val loader = SCScriptLoaderExternal(
            dirScripts,
            file.name,
            javaClass.classLoader
        )

        val clazz = loader.loadClass(
            PACKAGE + scriptName
        )

        val methodExecute = clazz.getMethod(
            SMETHOD_EXECUTE
        )

        val methodSetProvider = clazz.getMethod(
            SMETHOD_SET_PROVIDER,
            SDMProvider::class.java
        )

        val instance = clazz.newInstance()

        methodSetProvider.invoke(
            instance,
            SDMProvider(
                SDManagerProcessTime(
                    LinkedList()
                ),
                SDManagerLights(
                    LinkedList()
                )
            )
        )

        methodExecute.invoke(
            instance
        )

        // Associate sdk managers with engine managers
        // ....
        // (end)

        loader.removeScriptFromCache()
    }
}