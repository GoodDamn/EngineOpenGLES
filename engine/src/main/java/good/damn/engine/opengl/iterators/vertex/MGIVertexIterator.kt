package good.damn.engine.opengl.iterators.vertex

import good.damn.engine.opengl.arrays.MGArrayVertexManager


interface MGIVertexIterator {
    fun onEachVertex(
        index: Int,
        x: Int,
        z: Int,
        vertexArray: MGArrayVertexManager
    )
}