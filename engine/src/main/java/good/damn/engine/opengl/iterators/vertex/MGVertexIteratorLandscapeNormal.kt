package good.damn.engine.opengl.iterators.vertex

/*class MGVertexIteratorLandscapeNormal(
    private val mapNormal: MGMapNormal
): MGIVertexIterator {

    override fun onEachVertex(
        index: Int,
        x: Int,
        z: Int,
        vertexArray: MGArrayVertexManager
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

}*/