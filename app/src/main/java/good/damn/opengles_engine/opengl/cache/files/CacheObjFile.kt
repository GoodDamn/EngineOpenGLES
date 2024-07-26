package good.damn.opengles_engine.opengl.cache.files

import android.util.Log
import good.damn.opengles_engine.App
import java.io.File
import kotlin.math.log

class CacheObjFile(
    fileName: String
): File(
    "${App.DIR_CACHE}/$DIR/$fileName"
) {

    companion object {
        private const val TAG = "CacheObjFile"
        private const val DIR = "objs"
    }

    val dir = File(
        "${App.DIR_CACHE}/$DIR"
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