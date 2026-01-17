package good.damn.common

import java.io.File

object COMountDirectory {
    internal lateinit var DIRECTORY: File

    @JvmStatic
    fun mountDirectory(
        dir: File
    ) {
        DIRECTORY = dir
    }
}