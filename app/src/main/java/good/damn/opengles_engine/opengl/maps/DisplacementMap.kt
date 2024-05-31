package good.damn.opengles_engine.opengl.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import good.damn.opengles_engine.opengl.factories.SKBitmapFactory
import java.io.InputStream
import kotlin.math.roundToInt

class DisplacementMap(
    private val mBitmap: Bitmap
) {
    companion object {
        fun createFromStream(
            inp: InputStream
        ): DisplacementMap {
            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return DisplacementMap(
                b
            )
        }

        fun createFromAssets(
            assetPath: String
        ): DisplacementMap {
            return DisplacementMap(
                SKBitmapFactory
                    .createFromAssets(assetPath)
            )
        }

        private const val TAG = "DisplacementMap"
        private const val MAX_HEIGHT = 30.0f
    }

    private val mBitmapWidth = mBitmap.width
    private val mBitmapHeight = mBitmap.height

    fun getHeightRatio(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Float {
        return getHeight(
            (x / width.toFloat() * mBitmapWidth).toInt(),
            (y / height.toFloat() * mBitmapHeight).toInt()
        )
    }

    fun getHeight(
        x: Int,
        y: Int
    ): Float {
        val xx = if (x == mBitmapWidth) x-1
            else x
        val yy = if (y == mBitmapHeight) y-1
            else y
        val color = mBitmap
            .getPixel(xx,yy)

        val digitalHeight = color and 0xff

        val height = (digitalHeight / 255f) * MAX_HEIGHT
        return height
    }
}