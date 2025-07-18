package good.damn.engine.opengl.maps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import good.damn.engine.opengl.factories.MGFactoryBitmap
import java.io.InputStream
import kotlin.math.roundToInt

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
        private const val iDIGITAL_HEIGHT_MAX = 0x00ffffff
        private const val fDIGITAL_HEIGHT_MAX = iDIGITAL_HEIGHT_MAX.toFloat()
    }

    private val mBitmapWidth = mBitmap.width
    private val mBitmapHeight = mBitmap.height

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

        val digitalHeight = color and iDIGITAL_HEIGHT_MAX
        return digitalHeight / fDIGITAL_HEIGHT_MAX
    }
}