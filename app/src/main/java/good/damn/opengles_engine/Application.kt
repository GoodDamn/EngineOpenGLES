package good.damn.opengles_engine

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.widget.Toast
import java.nio.ByteOrder
import java.nio.charset.Charset

class Application
: Application() {

    companion object {
        lateinit var ASSETS: AssetManager
        lateinit var RESOURCES: Resources
        lateinit var CONTENT_RESOLVER: ContentResolver

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

        RESOURCES = context
            .resources

        CONTENT_RESOLVER = context
            .contentResolver

    }
}