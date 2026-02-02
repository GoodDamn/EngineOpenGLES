package good.damn.common.utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

object COUtilsInputStream {

    @JvmStatic
    fun readBytes(
        inp: InputStream,
        buffer: ByteArray
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