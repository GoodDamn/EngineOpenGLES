package good.damn.mapimporter.models

data class MIMAtlas(
    val height: Int,
    val name: String,
    val padding: Int,
    val rects: List<MIMAtlasRect>,
    val width: Int
)