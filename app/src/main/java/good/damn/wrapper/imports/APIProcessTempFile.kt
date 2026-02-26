package good.damn.wrapper.imports

import java.io.File

interface APIProcessTempFile {
    fun isValidExtension(
        fileName: String
    ): Boolean

    fun onProcessTempFile(
        rootFile: File,
        contextFiles: Array<File?>
    )
}