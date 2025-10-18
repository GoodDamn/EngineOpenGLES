package good.damn.engine.runnables

import androidx.lifecycle.viewmodel.viewModelFactory
import good.damn.engine.opengl.MGObject3d
import java.io.File

class MGRunnableImportModel(
    private val modelsCallback: MGICallbackModel,
    private val fileTemp: File
): Runnable {

    override fun run() {
        if (!fileTemp.exists()) {
            return
        }

        val arrModels = MGObject3d.createFromPath(
            fileTemp.path
        )

        modelsCallback.onGetObjects(
            arrModels
        )

        fileTemp.delete()
    }

}