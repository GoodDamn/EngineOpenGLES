package good.damn.engine.opengl.iterators.vertex

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.maps.MGMapDisplace

class MGVertexIteratorLandscapeDisplace(
    private val mapDisplace: MGMapDisplace,
    private val maxHeight: Int,
    private val width: Int,
    private val height: Int
): MGIVertexIterator {

    override fun onEachVertex(
        index: Int,
        x: Int,
        z: Int,
        vertexArray: MGArrayVertex
    ) {
        val topVert = mapDisplace.getHeightNormalRatio(
            x, z - 1,
            width, height
        )

        val leftVert = mapDisplace.getHeightNormalRatio(
            x - 1, z,
            width, height
        )

        val bottomVert = mapDisplace.getHeightNormalRatio(
            x, z + 1,
            width, height
        )

        val rightVert = mapDisplace.getHeightNormalRatio(
            x + 1, z,
            width, height
        )

        val middleVert = mapDisplace.getHeightNormalRatio(
            x, z,
            width, height
        )

        val smooth = (
            middleVert + topVert + rightVert + leftVert + bottomVert
        ) / 5f

        // Position Y
        vertexArray.writeVertexBufferData(
            index + 1,
            smooth * maxHeight
        )
    }

}