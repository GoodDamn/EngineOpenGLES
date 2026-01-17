package good.damn.wrapper.utils

import android.content.ContentResolver
import android.net.Uri
import good.damn.common.utils.COUtilsInputStream

object COUtilsContentResolver {
    @JvmStatic
    fun read(
        uri: Uri,
        contentResolver: ContentResolver,
        buffer: ByteArray
    ): ByteArray? {
        val inp = contentResolver.openInputStream(
            uri
        ) ?: return null

        val data = COUtilsInputStream.readBytes(
            inp,
            buffer
        )

        inp.close()

        return data
    }
}