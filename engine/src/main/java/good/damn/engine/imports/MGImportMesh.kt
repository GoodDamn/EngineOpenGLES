package good.damn.engine.imports

import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.runnables.MGICallbackModel
import java.io.File

class MGImportMesh(
    private val poolMeshes: MGPoolMeshesStatic,
    private val modelsCallback: MGICallbackModel
): MGImportFile {

    override fun onImportFile(
        it: File,
        buffer: ByteArray
    ) {
        poolMeshes[
            it.name
        ]?.run {
            modelsCallback.onGetObjectsCached(
                this
            )
            return
        }

        val arrModels = MGObject3d.createFromPath(
            it.path
        )

        modelsCallback.onGetObjects(
            it.name,
            arrModels
        )
    }

}