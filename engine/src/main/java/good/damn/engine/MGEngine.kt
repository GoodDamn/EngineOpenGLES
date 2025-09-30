package good.damn.engine

import android.content.Context
import android.content.res.AssetManager
import android.opengl.GLES30.GL_EXTENSIONS
import android.opengl.GLES30.glGetString
import android.os.Environment
import android.util.Log
import good.damn.engine.opengl.enums.MGEnumDrawMode
import java.io.File
import java.nio.ByteOrder
import java.nio.charset.Charset

class MGEngine {
    companion object {
        private const val TAG = "MGEngine"
        internal lateinit var ASSETS: AssetManager
        internal lateinit var DIR_CACHE: File
        internal lateinit var DIR_PUBLIC: File

        internal var drawMode = MGEnumDrawMode.OPAQUE

        internal val BYTE_ORDER = ByteOrder.nativeOrder()
        internal val BUFFER_MB = ByteArray(1024*1024)
        internal val CHARSET = Charset.forName("UTF-8")
        internal val CHARSET_ASCII = Charset.forName("US-ASCII")

        fun init(
            applicationContext: Context
        ) {
            ASSETS = applicationContext.assets
            DIR_PUBLIC = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )
            createPublicDir()
        }

        private inline fun createPublicDir() {
            File(
                DIR_CACHE,
                "MGDirectory"
            ).let {
                if (!it.exists() && it.mkdirs()) {
                    Log.d(TAG, "onSurfaceCreated: ${it.name} is created")
                }

                File(
                    it,
                    "extensions.txt"
                ).run {
                    if (exists() && length() != 0L) {
                        return@run
                    }

                    if (createNewFile()) {
                        Log.d(TAG, "onSurfaceCreated: $name is created")
                    }
                }

            }
        }
    }
}