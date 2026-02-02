package good.damn.wrapper.imports

import good.damn.wrapper.models.MGMUserContent

interface MGIImport {
    fun isValidExtension(
        fileName: String
    ): Boolean

    fun processUserContent(
        userContent: MGMUserContent
    )
}