package good.damn.engine.utils

import android.util.Log
import good.damn.engine.MGEngine
import java.io.File
import java.io.FileInputStream
import java.nio.file.NoSuchFileException

class MGUtilsAsset {

    companion object {
        private const val TAG = "MGUtilsAsset"

        fun loadString(
            path: String
        ): String {
            val pubFile = File(
                MGEngine.DIR_PUBLIC,
                path
            )

            if (!pubFile.exists()) {
                throw Exception(pubFile.path)
            }

            val inp = FileInputStream(
                pubFile
            )

            val b = MGUtilsFile.readBytes(
                inp
            )

            inp.close()

            return String(
                b,
                MGEngine.getCharsetUTF8()
            )
        }

    }

}