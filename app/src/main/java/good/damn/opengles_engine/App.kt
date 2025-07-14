package good.damn.opengles_engine

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.widget.Toast
import good.damn.engine.MGEngine
import java.io.File
import java.nio.ByteOrder
import java.nio.charset.Charset

class App
: Application() {

    companion object {
        var WIDTH = 1
        var HEIGHT = 1

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

        MGEngine.init(
            context
        )

        val metrics = resources
            .displayMetrics

        HEIGHT = metrics.heightPixels
        WIDTH = metrics.widthPixels
    }
}