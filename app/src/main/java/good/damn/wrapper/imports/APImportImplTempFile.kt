package good.damn.wrapper.imports

import good.damn.common.extensions.copyTo
import good.damn.wrapper.models.APMUserContent
import good.damn.wrapper.APApp
import java.io.File

class APImportImplTempFile(
    private val processTempFile: APIProcessTempFile
): APIImport {

    override fun processUserContent(
        userContent: APMUserContent,
        contextUserContents: Array<APMUserContent?>,
        offsetContextUserContents: Int
    ) {
        val tempFile = createTempFile(
            userContent
        ) ?: return

        val contextTempFiles = Array(
            contextUserContents.size - offsetContextUserContents
        ) {
            contextUserContents[it + offsetContextUserContents]?.run {
                createTempFile(
                    this
                )
            }
        }

        processTempFile.onProcessTempFile(
            tempFile,
            contextTempFiles
        )
    }

    override fun isValidExtension(
        fileName: String
    ) = processTempFile.isValidExtension(
        fileName
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