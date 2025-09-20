package good.damn.engine.opengl.iterators.vertex

import good.damn.engine.opengl.MGArrayVertex

interface MGIVertexIterator {
    fun onEachVertex(
        index: Int,
        x: Int,
        z: Int,
        vertexArray: MGArrayVertex
    )
}