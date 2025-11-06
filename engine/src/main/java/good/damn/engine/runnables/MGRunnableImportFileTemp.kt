package good.damn.engine.runnables

import good.damn.engine.imports.MGImportFile
import java.io.File

class MGRunnableImportFileTemp(
    private val importer: MGImportFile
): Runnable {
    var fileTemp: File? = null

    override fun run() {
        fileTemp?.run {
            fileTemp = null
            if (!exists()) {
                return
            }

            importer.onImportFile(
                this
            )

            delete()
        }
    }
}