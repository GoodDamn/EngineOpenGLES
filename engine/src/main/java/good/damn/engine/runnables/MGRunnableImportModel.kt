package good.damn.engine.runnables

import androidx.lifecycle.viewmodel.viewModelFactory
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import java.io.File

class MGRunnableImportModel(
    private val modelsCallback: MGICallbackModel,
    private val poolMeshes: MGPoolMeshesStatic,
    private val fileTemp: File
): Runnable {

    override fun run() {
        if (!fileTemp.exists()) {
            return
        }

        poolMeshes[
            fileTemp.name
        ]?.run {
            fileTemp.delete()
            modelsCallback.onGetObjectsCached(
                this
            )
            return
        }

        val arrModels = MGObject3d.createFromPath(
            fileTemp.path
        )

        modelsCallback.onGetObjects(
            fileTemp.name,
            arrModels
        )

        fileTemp.delete()
    }

}