package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGArrayVertexInstanced
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation

class MGDrawerMeshInstanced(
    private val vertexArray: MGArrayVertexInstanced
): MGIDrawer {

    override fun draw() {
        vertexArray.drawInstanced()
    }
}