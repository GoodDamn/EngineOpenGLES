package good.damn.wrapper.imports

import android.os.Handler

data class APMImportMisc(
    val handler: Handler,
    val modelsCallback: APCallbackModelSpawn,
    val buffer: ByteArray,
)