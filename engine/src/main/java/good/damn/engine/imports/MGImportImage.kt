package good.damn.engine.imports

import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.pools.MGPoolTextures
import java.io.File

class MGImportImage(
    private val poolTextures: MGPoolTextures,
    private val misc: MGMImportMisc
): MGImportImplTempFile() {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".jpg"
    )

    override fun onProcessTempFile(
        file: File
    ) {

    }
}