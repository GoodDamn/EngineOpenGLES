package good.damn.opengles_engine.opengl.factories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import good.damn.opengles_engine.App

class SKBitmapFactory {
    companion object {
        fun createFromAssets(
            assetPath: String
        ): Bitmap {
            val inp = App.ASSETS
                .open(assetPath)

            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return b
        }
    }
}