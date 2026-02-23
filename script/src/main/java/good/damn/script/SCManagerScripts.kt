package good.damn.script

import good.damn.engine.sdk.models.provider.SDMProvider
import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

class SCManagerScripts(
    private val dirScripts: File
) {

    private companion object {
        private const val PACKAGE = "sdk.engine."
        private const val SMETHOD_EXECUTE = "execute"
        private const val SMETHOD_SET_PROVIDER = "setSdProvider"
        private const val EXTENSION = ".jar"
    }

    @Throws(IOException::class)
    fun load(
        scriptName: String,
        providerModel: SDMProvider
    ) {
        val loader = SCScriptLoaderExternal(
            dirScripts,
            scriptName + EXTENSION,
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
            providerModel
        )

        methodExecute.invoke(
            instance
        )

        loader.removeScriptFromCache()
    }
}