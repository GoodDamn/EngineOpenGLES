package good.damn.engine.imports

import android.os.Handler
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.runnables.MGICallbackModel
import java.io.File

class MGImportImplModel(
    private val misc: MGMImportMisc
): MGImportImplTempFile() {

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
        misc.poolMeshes[
            file.name
        ]?.run {
            misc.modelsCallback.onGetObjectsCached(
                this
            )
            file.delete()
            return
        }
        misc.handler.post(
            MGRunnableLoadModel(
                misc.modelsCallback,
                file
            )
        )
    }


    private class MGRunnableLoadModel(
        private val modelsCallback: MGICallbackModel,
        private val file: File
    ): Runnable {
        override fun run() {
            val arrModels = MGObject3d.createFromPath(
                file.path
            )

            modelsCallback.onGetObjects(
                file.name,
                arrModels
            )
            file.delete()
        }
    }
}