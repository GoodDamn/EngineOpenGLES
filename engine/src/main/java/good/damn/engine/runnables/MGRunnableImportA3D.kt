package good.damn.engine.runnables

import good.damn.engine.imports.MGImportA3D
import good.damn.ia3d.models.A3DMAsset

class MGRunnableImportA3D(
    private val importer: MGImportA3D
): Runnable {
    var asset: A3DMAsset? = null
    var fileName: String? = null

    override fun run() {
        asset?.run {
            val fileName = fileName
                ?: return

            importer.import(
                fileName,
                this
            )
        }
    }
}