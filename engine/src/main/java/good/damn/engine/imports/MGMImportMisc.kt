package good.damn.engine.imports

import android.os.Handler
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.runnables.MGICallbackModel

data class MGMImportMisc(
    val handler: Handler,
    val modelsCallback: MGICallbackModel,
    val buffer: ByteArray
)