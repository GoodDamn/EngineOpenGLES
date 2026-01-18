package good.damn.engine2

import java.io.File

object MGMountDirectory {
    internal lateinit var DIRECTORY: File

    @JvmStatic
    fun mountDirectory(
        file: File
    ) {
        DIRECTORY = file
    }
}