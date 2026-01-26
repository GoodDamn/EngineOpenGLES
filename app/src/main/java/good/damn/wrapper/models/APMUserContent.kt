package good.damn.wrapper.models

import java.io.InputStream

data class APMUserContent(
    val fileName: String,
    val mimeType: String,
    val stream: InputStream
)