package good.damn.opengles_engine.utils

import good.damn.opengles_engine.App

class AssetUtils {

    companion object {

        fun loadString(
            path: String
        ): String {
            val inp = App.ASSETS
                .open(path)

            val b = FileUtils
                .readBytes(inp)
            inp.close()

            return String(
                b,
                App.CHARSET
            )
        }

    }

}