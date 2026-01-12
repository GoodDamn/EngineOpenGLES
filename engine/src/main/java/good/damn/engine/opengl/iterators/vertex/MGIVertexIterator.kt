package good.damn.engine.opengl.iterators.vertex

import good.damn.common.vertex.COMArrayVertexManager


interface MGIVertexIterator {
    fun onEachVertex(
        index: Int,
        x: Int,
        z: Int,
        vertexArray: COMArrayVertexManager
    )
}