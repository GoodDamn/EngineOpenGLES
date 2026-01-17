package good.damn.script

import android.content.Context
import java.io.File

object SCMountDirectory {
    internal lateinit var DIRECTORY: File
    internal lateinit var DIRECTORY_CODE: File

    @JvmStatic
    fun mountDirectory(
        context: Context
    ) {
        DIRECTORY = context.cacheDir
        DIRECTORY_CODE = context.codeCacheDir
    }
}