package good.damn.script

import dalvik.system.DexClassLoader
import good.damn.common.extensions.writeToFile
import java.io.File

class SCScriptLoaderExternal(
    externalDirScripts: File,
    private val scriptFileName: String,
    classLoader: ClassLoader?
): DexClassLoader(
    File(
        SCMountDirectory.DIRECTORY,
        scriptFileName
    ).apply {
        delete()
        File(
            externalDirScripts,
            scriptFileName
        ).inputStream().writeToFile(
            this
        )
        setReadOnly()
    }.path,
    SCMountDirectory.DIRECTORY_CODE.path,
    null,
    classLoader
) {
    fun removeScriptFromCache() = File(
        SCMountDirectory.DIRECTORY,
        scriptFileName
    ).delete()
}