package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGDrawerMeshInstanced(
    private val vertexArray: MGArrayVertexInstanced,
    private val material: MGMaterial
) {
    fun draw(
        shaderMaterial: MGShaderMaterial
    ) {
        material.draw(
            shaderMaterial
        )
        vertexArray.drawInstanced()
        material.unbind(
            shaderMaterial
        )
    }
}