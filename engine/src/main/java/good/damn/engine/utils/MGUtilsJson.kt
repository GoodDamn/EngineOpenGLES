package good.damn.engine.utils

import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

object MGUtilsJson {

    @JvmStatic
    fun createFromStream(
        stream: InputStream
    ) = JSONObject(
        String(
            stream.run {
                val b = readBytes()
                close()
                return@run b
            },
            Charset.forName(
                "UTF-8"
            )
        )
    )

    @JvmStatic
    fun createFromFile(
        file: File
    ) = createFromStream(
        file.inputStream()
    )
}