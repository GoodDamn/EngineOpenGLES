package good.damn.engine.imports

import good.damn.engine.opengl.models.MGMUserContent
import java.io.InputStream

interface MGIImport {
    fun isValidExtension(
        fileName: String
    ): Boolean

    fun processUserContent(
        userContent: MGMUserContent
    )
}