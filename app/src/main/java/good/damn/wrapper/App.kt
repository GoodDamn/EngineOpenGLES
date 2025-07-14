package good.damn.wrapper

import android.app.Application
import android.content.Context
import android.widget.Toast
import good.damn.engine.MGEngine

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