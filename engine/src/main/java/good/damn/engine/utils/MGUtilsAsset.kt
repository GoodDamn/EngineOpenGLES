package good.damn.engine.utils

import good.damn.engine.MGEngine
import java.io.File
import java.io.FileInputStream

object MGUtilsAsset {
    private const val TAG = "MGUtilsAsset"

    @JvmStatic
    fun loadString(
        file: File
    ): String {
        val inp = FileInputStream(
            file
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

    @JvmStatic
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

        return loadString(
            pubFile
        )
    }
}