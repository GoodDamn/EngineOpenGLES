package good.damn.engine.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.ColorInt
import java.io.FileInputStream

object MGUtilsBitmap {

    @JvmStatic
    fun generateFromColor(
        @ColorInt color: Int
    ) = Bitmap.createBitmap(
        intArrayOf(
            color
        ),
        1, 1,
        Bitmap.Config.ARGB_8888
    )

    @JvmStatic
    fun loadBitmap(
        localPath: String,
        textureName: String
    ) = loadBitmap(
        "$localPath/$textureName"
    )

    @JvmStatic
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