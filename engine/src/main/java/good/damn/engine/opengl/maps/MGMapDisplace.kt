package good.damn.engine.opengl.maps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import good.damn.engine.opengl.factories.MGFactoryBitmap
import java.io.InputStream

class MGMapDisplace(
    private val mBitmap: Bitmap
) {
    companion object {
        fun createFromStream(
            inp: InputStream
        ): MGMapDisplace {
            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return MGMapDisplace(
                b
            )
        }

        fun createFromAssets(
            assetPath: String
        ): MGMapDisplace {
            return MGMapDisplace(
                MGFactoryBitmap
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
    ) = getHeightNormal(
        (x / width.toFloat() * mBitmapWidth).toInt(),
        (y / height.toFloat() * mBitmapHeight).toInt()
    ) * MAX_HEIGHT

    fun getHeightNormalRatio(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) = getHeightNormal(
        (x / width.toFloat() * mBitmapWidth).toInt(),
        (y / height.toFloat() * mBitmapHeight).toInt()
    )

    fun getHeightNormal(
        x: Int,
        y: Int
    ): Float {
        val xx = when {
            x >= mBitmapWidth -> mBitmapWidth - 1
            x < 0 -> 0
            else -> x
        }

        val yy = when {
            y >= mBitmapHeight -> mBitmapHeight - 1
            y < 0 -> 0
            else -> y
        }

        val color = mBitmap
            .getPixel(xx,yy)

        val digitalHeight = color and 0xff
        return digitalHeight / 255f
    }
}