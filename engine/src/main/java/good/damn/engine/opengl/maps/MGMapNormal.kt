package good.damn.engine.opengl.maps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.get
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.factories.MGFactoryBitmap
import java.io.InputStream

class MGMapNormal(
    private val bitmap: Bitmap
) {
    companion object {
        fun createFromStream(
            inp: InputStream
        ): MGMapNormal {
            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return MGMapNormal(
                b
            )
        }

        fun createFromAssets(
            assetPath: String
        ): MGMapNormal {
            return MGMapNormal(
                MGFactoryBitmap
                    .createFromAssets(assetPath)
            )
        }
    }

    fun getNormalRatio(
        x: Int,
        y: Int
    ) = getNormal(
        x.toFloat() / bitmap.width,
        y.toFloat() / bitmap.height
    )

    fun getNormal(
        nx: Float,
        ny: Float
    ) = bitmap.getPixel(
        (nx * bitmap.width).toInt(),
        (ny * bitmap.height).toInt()
    ).run {
        MGVector(
            normalize(
                0x00ff0000,
                16
            ),
            normalize(
                0x0000ff00,
                8
            ),
            normalize(
                0x000000ff,
                0
            )
        )
    }

    fun destroy() {
        bitmap.recycle()
    }

    private inline fun Int.normalize(
        mask: Int,
        offset: Int
    ) = (((this and mask) shr offset) - 127) / 255f

}