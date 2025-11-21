package good.damn.engine.runnables

import android.util.Log
import good.damn.engine.imports.MGImportFile
import java.io.File

class MGRunnableImportFileTemp(
    private val importer: MGImportFile,
    private val buffer: ByteArray
): Runnable {
    var fileTemp: File? = null

    override fun run() {
        fileTemp?.run {

            if (!exists()) {
                return
            }

            importer.onImportFile(
                this,
                buffer
            )

            delete()
        }

        fileTemp = null
    }
}