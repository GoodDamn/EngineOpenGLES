package good.damn.engine.opengl.models

import java.io.InputStream

data class MGMUserContent(
    val fileName: String,
    val mimeType: String,
    val stream: InputStream
)