package good.damn.engine.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.ColorInt
import java.io.FileInputStream

object MGUtilsBitmap {

    fun generateFromColor(
        @ColorInt color: Int
    ) = Bitmap.createBitmap(
        intArrayOf(
            color
        ),
        1, 1,
        Bitmap.Config.ARGB_8888
    )

    fun loadBitmap(
        localPath: String
    ): Bitmap? {
        val filePub = MGUtilsFile.getPublicFile(
            localPath
        )

        if (!filePub.exists()) {
            return null
        }

        return BitmapFactory.decodeStream(
            FileInputStream(
                filePub
            )
        )
    }

}