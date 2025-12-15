package good.damn.engine

import android.content.Context
import android.content.res.AssetManager
import android.opengl.GLES30.GL_EXTENSIONS
import android.opengl.GLES30.glGetString
import android.os.Environment
import android.util.Log
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.shader.MGShaderSource
import java.io.File
import java.nio.ByteOrder
import java.nio.charset.Charset

class MGEngine {
    companion object {
        private const val TAG = "MGEngine"
        private val CHARSET = Charset.forName("UTF-8")

        internal lateinit var DIR_CACHE: File
        internal lateinit var DIR_PUBLIC: File
        internal lateinit var DIR_PUBLIC_TEMP: File
        internal lateinit var DIR_DATA: File

        internal lateinit var shaderSource: MGShaderSource

        internal var drawMode = MGEnumDrawMode.OPAQUE

        internal val BYTE_ORDER = ByteOrder.nativeOrder()
        internal val BUFFER_MB = ByteArray(1024*1024)
        internal val CHARSET_ASCII = Charset.forName("US-ASCII")

        fun getCharsetUTF8() = CHARSET

        fun init(
            applicationContext: Context
        ) {
            DIR_PUBLIC = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                ),
                "MGDirectory"
            )

            DIR_DATA = File(
                applicationContext
                .applicationInfo
                .dataDir
            )

            DIR_PUBLIC_TEMP = File(
                DIR_PUBLIC,
                "temp"
            )

            createPublicDir(
                DIR_PUBLIC
            )

            createPublicDir(
                DIR_PUBLIC_TEMP
            )
        }

        private inline fun createPublicDir(
            it: File
        ) {
            if (!it.exists() && it.mkdirs()) {
                Log.d(TAG, "onSurfaceCreated: ${it.name} is created")
            }
        }
    }
}