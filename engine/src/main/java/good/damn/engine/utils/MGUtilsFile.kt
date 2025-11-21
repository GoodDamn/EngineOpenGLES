package good.damn.engine.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.utils.MGUtilsAsset.Companion
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object MGUtilsFile {
    fun getPublicFile(
        localPath: String
    ) = File(
        MGEngine.DIR_PUBLIC,
        localPath
    )

    fun read(
        uri: Uri?,
        context: Context
    ): ByteArray? {
        if (uri == null) {
            return null
        }

        val inp = context.contentResolver
            .openInputStream(uri) ?: return null

        val data = readBytes(inp)

        inp.close()

        return data
    }


    fun readBytes(
        inp: InputStream,
        buffer: ByteArray = MGEngine.BUFFER_MB
    ): ByteArray {

        val outArr = ByteArrayOutputStream()

        var n: Int

        while (true) {
            n = inp.read(buffer)
            if (n == -1) {
                break
            }
            outArr.write(buffer,0,n)
        }

        val data = outArr.toByteArray()
        outArr.close()

        return data
    }
}