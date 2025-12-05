package good.damn.ia3d.extractors

internal interface A3DIExtractor {

    val indicesCount: Int

    val vertex0: Int
    val vertex1: Int
    val vertex2: Int

    fun extract(
        startIndex: Int
    )
}