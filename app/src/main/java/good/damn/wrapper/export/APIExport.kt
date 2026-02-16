package good.damn.wrapper.export

import java.io.File

interface APIExport {
    fun export(
        file: File
    )
}