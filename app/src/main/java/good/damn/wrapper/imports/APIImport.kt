package good.damn.wrapper.imports

import good.damn.wrapper.models.APMUserContent

interface APIImport {
    fun isValidExtension(
        fileName: String
    ): Boolean

    fun processUserContent(
        userContent: APMUserContent,
        contextUserContents: Array<APMUserContent?>,
        offsetContextUserContents: Int
    )
}