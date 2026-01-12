package good.damn.wrapper.imports

import good.damn.engine.opengl.models.MGMUserContent

interface MGIImport {
    fun isValidExtension(
        fileName: String
    ): Boolean

    fun processUserContent(
        userContent: MGMUserContent
    )
}