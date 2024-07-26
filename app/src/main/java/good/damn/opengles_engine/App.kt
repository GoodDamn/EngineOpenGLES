package good.damn.opengles_engine

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.widget.Toast
import java.io.File
import java.nio.ByteOrder
import java.nio.charset.Charset

class App
: Application() {

    companion object {
        lateinit var ASSETS: AssetManager
        lateinit var DIR_CACHE: File

        var WIDTH = 1
        var HEIGHT = 1

        val BYTE_ORDER = ByteOrder
            .nativeOrder()
        val BUFFER_MB = ByteArray(1024*1024)
        val CHARSET = Charset.forName("UTF-8")
        val CHARSET_ASCII = Charset.forName("US-ASCII")

        fun showMessage(
            msg: String,
            context: Context
        ) {
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context = this

        ASSETS = context
            .assets

        val metrics = resources
            .displayMetrics

        HEIGHT = metrics.heightPixels
        WIDTH = metrics.widthPixels

        DIR_CACHE = context.cacheDir

    }
}