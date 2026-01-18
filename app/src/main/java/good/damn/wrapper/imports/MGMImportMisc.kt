package good.damn.wrapper.imports

import android.os.Handler

data class MGMImportMisc(
    val handler: Handler,
    val modelsCallback: MGCallbackModelSpawn,
    val buffer: ByteArray,
)