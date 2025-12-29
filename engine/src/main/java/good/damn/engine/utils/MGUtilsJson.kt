package good.damn.engine.utils

import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset

object MGUtilsJson {

    @JvmStatic
    fun createFromFile(
        file: File
    ) = JSONObject(
        String(
            file.inputStream().run {
                val b = readBytes()
                close()
                return@run b
            },
            Charset.forName("UTF-8")
        )
    )
}