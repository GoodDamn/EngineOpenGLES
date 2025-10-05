package good.damn.engine.utils

import android.util.Log
import good.damn.engine.MGEngine
import java.io.File
import java.io.FileInputStream

class MGUtilsAsset {

    companion object {
        private const val TAG = "MGUtilsAsset"

        fun createShaderPublicDir() {
            val dir = File(
                MGEngine.DIR_PUBLIC,
                "shaders"
            )

            if (!dir.exists() && dir.mkdirs()) {
                Log.d(TAG, "copyShaderCodeToExternal: ${dir.name} is created")
            }
        }

        fun loadString(
            path: String
        ): String {
            val pubFile = File(
                MGEngine.DIR_PUBLIC,
                path
            )

            val inp = if (
                pubFile.exists()
            ) FileInputStream(
                pubFile
            ) else MGEngine.ASSETS.open(
                path
            )

            val b = MGUtilsFile.readBytes(
                inp
            )

            inp.close()

            return String(
                b,
                MGEngine.CHARSET
            )
        }

    }

}