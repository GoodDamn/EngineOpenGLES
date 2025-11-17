package good.damn.engine.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream

object MGUtilsBitmap {

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