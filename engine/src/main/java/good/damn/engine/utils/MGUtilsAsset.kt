package good.damn.engine.utils

import good.damn.engine.MGEngine

class MGUtilsAsset {

    companion object {

        fun loadString(
            path: String
        ): String {
            val inp = MGEngine.ASSETS.open(
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