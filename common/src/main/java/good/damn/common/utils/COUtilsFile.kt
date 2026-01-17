package good.damn.common.utils

import good.damn.common.COMountDirectory
import java.io.File

object COUtilsFile {

    @JvmStatic
    fun getPublicFile(
        localPath: String
    ) = File(
        COMountDirectory.DIRECTORY,
        localPath
    )

}