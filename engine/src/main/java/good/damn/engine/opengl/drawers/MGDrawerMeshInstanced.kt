package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation

class MGDrawerMeshInstanced(
    private val vertexArray: MGArrayVertexInstanced,
    private val material: MGMaterial
): MGIDrawer {

    override fun draw() {
        material.draw()
        vertexArray.drawInstanced()
        material.unbind()
    }
}