package good.damn.wrapper.hud.ui.clicks

import android.util.Log
import good.damn.hud.UIIClick
import good.damn.wrapper.APApp
import good.damn.wrapper.export.APIExport
import java.io.File

class APClickExport(
    private val export: APIExport
): UIIClick {

    companion object {
        private const val TAG = "APClickExport"
        private const val DIR_NAME = "export"
        private const val FILE_NAME_POINT_LIGHTS = "pointLights"
    }

    private val mDir = File(
        APApp.DIR_PUBLIC,
        DIR_NAME
    ).apply {
        if (!exists() && mkdirs()) {
            Log.d(TAG, "init: dir $name is created")
        }
    }

    override fun onClick() {
        export.export(
            File(
                mDir,
                FILE_NAME_POINT_LIGHTS
            )
        )
    }
}