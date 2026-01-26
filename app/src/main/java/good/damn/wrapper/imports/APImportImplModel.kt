package good.damn.wrapper.imports

import good.damn.engine.ASObject3d
import java.io.File

class APImportImplModel(
    private val misc: APMImportMisc
): APImportImplTempFile() {

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".fbx"
    ) || fileName.contains(
        ".obj"
    ) || fileName.contains(
        ".3ds"
    )

    override fun onProcessTempFile(
        file: File
    ) {
        misc.handler.post(
            MGRunnableLoadModel(
                misc.modelsCallback,
                file
            )
        )
    }

    private class MGRunnableLoadModel(
        private val modelsCallback: APCallbackModelSpawn,
        private val file: File
    ): Runnable {
        override fun run() {
            val arrModels = ASObject3d.createFromPath(
                file.path
            )

            modelsCallback.processObjects(
                file.name,
                arrModels
            )
            file.delete()
        }
    }
}