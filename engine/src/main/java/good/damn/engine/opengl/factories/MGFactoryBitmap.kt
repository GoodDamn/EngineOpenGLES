package good.damn.engine.opengl.factories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import good.damn.engine.MGEngine

class MGFactoryBitmap {
    companion object {
        fun createFromAssets(
            assetPath: String
        ): Bitmap {
            val inp = MGEngine.ASSETS
                .open(assetPath)

            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return b
        }
    }
}