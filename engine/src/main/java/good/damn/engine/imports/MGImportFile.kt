package good.damn.engine.imports

import java.io.File

interface MGImportFile {
    fun onImportFile(
        it: File
    )
}