package good.damn.common.extensions

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

fun InputStream.writeToFile(
    file: File
) {
    if (!file.exists() && file.createNewFile()) {
        Log.d("InputStream", "writeToFile: $file is created")
    }
    copyTo(
        FileOutputStream(
            file
        )
    )
}

fun InputStream.copyTo(
    os: OutputStream
) {
    val buffer = ByteArray(8192)
    var n: Int
    while (true) {
        n = read(buffer)
        if (n == -1) {
            break
        }
        os.write(
            buffer,
            0,
            n
        )
    }

    close()
    os.close()
}