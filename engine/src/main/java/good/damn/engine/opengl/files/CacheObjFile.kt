package good.damn.engine.opengl.files

import android.util.Log
import good.damn.engine.MGEngine
import java.io.File

class CacheObjFile(
    fileName: String
): File(
    "${MGEngine.DIR_CACHE}/$DIR/$fileName"
) {

    companion object {
        private const val TAG = "CacheObjFile"
        private const val DIR = "objs"
    }

    val dir = File(
        "${MGEngine.DIR_CACHE}/$DIR"
    )

    init {

        if (dir.mkdirs()) {
            Log.d(TAG, "DIR: $dir created")
        }

        if (!exists() && createNewFile()) {
            Log.d(TAG, "FILE: $this created")
        }

    }

}