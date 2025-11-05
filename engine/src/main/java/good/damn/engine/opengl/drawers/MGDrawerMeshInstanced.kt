package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation

class MGDrawerMeshInstanced(
    private val vertexArray: MGArrayVertex,
    private val modelMatrices: Array<MGMatrixScaleRotation>
): MGIDrawer {

    override fun draw() {
        vertexArray.drawInstanced(
            count = modelMatrices.size
        )
    }
}