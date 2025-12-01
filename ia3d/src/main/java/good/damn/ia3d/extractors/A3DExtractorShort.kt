package good.damn.ia3d.extractors

import java.nio.ShortBuffer

internal class A3DExtractorShort(
    private val buffer: ShortBuffer
): A3DIExtractor {

    override val indicesCount = buffer.capacity()

    override var vertex0 = 0
        private set

    override var vertex1 = 0
        private set

    override var vertex2 = 0
        private set

    override fun extract(
        startIndex: Int
    ) {
        vertex0 = buffer[startIndex].toInt()
        vertex1 = buffer[startIndex+1].toInt()
        vertex2 = buffer[startIndex+2].toInt()
    }
}