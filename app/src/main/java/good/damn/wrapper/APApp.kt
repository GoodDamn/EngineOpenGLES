package good.damn.wrapper

import android.app.Application
import android.os.Environment
import android.util.Log
import good.damn.common.COMountDirectory
import good.damn.engine2.MGMountDirectory
import good.damn.script.SCMountDirectory
import java.io.File

class APApp
: Application() {

    companion object {
        var WIDTH = 1
        var HEIGHT = 1

        lateinit var DIR_PUBLIC_TEMP: File
        lateinit var DIR_PUBLIC: File
    }

    override fun onCreate() {
        super.onCreate()
        val context = this
        DIR_PUBLIC = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            ),
            "MGDirectory"
        )

        DIR_PUBLIC_TEMP = File(
            DIR_PUBLIC,
            "temp"
        )

        DIR_PUBLIC.createDir()
        DIR_PUBLIC_TEMP.createDir()

        SCMountDirectory.mountDirectory(
            context
        )

        MGMountDirectory.mountDirectory(
            DIR_PUBLIC
        )

        COMountDirectory.mountDirectory(
            DIR_PUBLIC
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