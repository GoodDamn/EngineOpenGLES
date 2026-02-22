package good.damn.script

import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

class SCManagerScripts(
    private val dirScripts: File
) {

    private companion object {
        private const val PACKAGE = "sdk.engine."
        private const val SMETHOD_EXECUTE = "execute"
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

        val instance = clazz.newInstance()
        methodExecute.invoke(
            instance
        )
    }
}