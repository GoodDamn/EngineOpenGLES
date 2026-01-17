package good.damn.wrapper

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import good.damn.common.COMountDirectory
import good.damn.engine.MGEngine
import good.damn.engine.MGMountDirectory
import good.damn.script.SCMountDirectory
import java.io.File
import kotlin.system.exitProcess

class APApp
: Application() {

    companion object {
        var WIDTH = 1
        var HEIGHT = 1

        lateinit var DIR_PUBLIC_TEMP: File
    }

    override fun onCreate() {
        super.onCreate()
        val context = this
        val dirPublic = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            ),
            "MGDirectory"
        )

        DIR_PUBLIC_TEMP = File(
            dirPublic,
            "temp"
        )

        dirPublic.createDir()
        DIR_PUBLIC_TEMP.createDir()

        SCMountDirectory.mountDirectory(
            context
        )

        MGMountDirectory.mountDirectory(
            dirPublic
        )

        COMountDirectory.mountDirectory(
            dirPublic
        )

        val metrics = resources
            .displayMetrics

        HEIGHT = metrics.heightPixels
        WIDTH = metrics.widthPixels
    }

    private inline fun File.createDir() {
        if (!exists() && mkdirs()) {
            Log.d("APApp", "createDir: $name is created")
        }
    }
}