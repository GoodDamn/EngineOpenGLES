package good.damn.opengles_engine.utils

import good.damn.opengles_engine.Application

class AssetUtils {

    companion object {

        fun loadString(
            path: String
        ): String {
            val inp = Application.ASSETS
                .open(path)

            val b = FileUtils
                .readBytes(inp)
            inp.close()

            return String(
                b,
                Application.CHARSET
            )
        }

    }

}