package good.damn.engine.scripts

import dalvik.system.DexClassLoader
import good.damn.engine.MGEngine
import good.damn.engine.opengl.extensions.writeToFile
import java.io.File

class MGLoaderScriptExternal(
    externalDirScripts: File,
    private val scriptFileName: String,
    classLoader: ClassLoader?
): DexClassLoader(
    File(
        MGEngine.DIR_CACHE,
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
    MGEngine.DIR_CODE_CACHE.path,
    null,
    classLoader
) {
    fun removeScriptFromCache() = File(
        MGEngine.DIR_CACHE,
        scriptFileName
    ).delete()
}