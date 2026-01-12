package good.damn.engine.opengl.factories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import good.damn.engine.utils.MGUtilsFile
import java.io.FileInputStream

class MGFactoryBitmap {
    companion object {
        fun createFromAssets(
            assetPath: String
        ): Bitmap {
            val filePub = MGUtilsFile.getPublicFile(
                assetPath
            )

            if (!filePub.exists()) {
                throw Exception("No such file: ${filePub.path}")
            }

            val inp = FileInputStream(
                filePub
            )

            val b = BitmapFactory
                .decodeStream(inp)

            inp.close()

            return b
        }
    }
}