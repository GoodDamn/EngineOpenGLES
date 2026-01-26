package good.damn.wrapper.imports

import good.damn.common.extensions.copyTo
import good.damn.wrapper.models.APMUserContent
import good.damn.wrapper.APApp
import java.io.File

abstract class APImportImplTempFile
: APIImport {

    final override fun processUserContent(
        userContent: APMUserContent
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
        userContent: APMUserContent
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