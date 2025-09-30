package good.damn.engine.opengl.iterators.vertex

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.maps.MGMapNormal

class MGVertexIteratorLandscapeNormal(
    private val mapNormal: MGMapNormal
): MGIVertexIterator {

    override fun onEachVertex(
        index: Int,
        x: Int,
        z: Int,
        vertexArray: MGArrayVertex
    ) {
        val norm = mapNormal.getNormalRatio(
            x, z
        )

        // Normal X
        vertexArray.writeVertexBufferData(
            index + 5,
            norm.y
        )
        // Normal Y
        vertexArray.writeVertexBufferData(
            index + 6,
            norm.z
        )
        // Normal Z
        vertexArray.writeVertexBufferData(
            index + 7,
            norm.x
        )
    }

}