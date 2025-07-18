package good.damn.engine

import android.content.Context
import android.content.res.AssetManager
import java.io.File
import java.nio.ByteOrder
import java.nio.charset.Charset

class MGEngine {
    companion object {
        internal lateinit var ASSETS: AssetManager
        internal lateinit var DIR_CACHE: File

        internal var isWireframe = false

        internal val BYTE_ORDER = ByteOrder.nativeOrder()
        internal val BUFFER_MB = ByteArray(1024*1024)
        internal val CHARSET = Charset.forName("UTF-8")
        internal val CHARSET_ASCII = Charset.forName("US-ASCII")

        fun init(
            applicationContext: Context
        ) {
            ASSETS = applicationContext.assets
            DIR_CACHE = applicationContext.cacheDir
        }
    }
}