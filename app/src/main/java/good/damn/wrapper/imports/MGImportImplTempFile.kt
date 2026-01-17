package good.damn.wrapper.imports

import good.damn.engine.MGEngine
import good.damn.engine.opengl.extensions.copyTo
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.wrapper.APApp
import java.io.File

abstract class MGImportImplTempFile
: MGIImport {

    final override fun processUserContent(
        userContent: MGMUserContent
    ) {
        createTempFile(
            userContent
        )?.apply {
            onProcessTempFile(this)
        }
    }

    protected abstract fun onProcessTempFile(
        file: File
    )

    private inline fun createTempFile(
        userContent: MGMUserContent
    ): File? {
        val temp = File(
            APApp.DIR_PUBLIC_TEMP,
            userContent.fileName
        )

        if (temp.exists()) {
            temp.delete()
        }

        if (!temp.createNewFile()) {
            return null
        }

        userContent.stream.copyTo(
            temp.outputStream()
        )

        return temp
    }
}