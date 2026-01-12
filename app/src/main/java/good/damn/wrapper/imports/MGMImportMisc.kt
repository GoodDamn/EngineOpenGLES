package good.damn.wrapper.imports

import android.os.Handler
import good.damn.engine.opengl.pools.MGPoolMeshesStatic

data class MGMImportMisc(
    val handler: Handler,
    val modelsCallback: MGCallbackModelSpawn,
    val buffer: ByteArray
)