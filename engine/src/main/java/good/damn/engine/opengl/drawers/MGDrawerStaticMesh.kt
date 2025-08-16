package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerStaticMesh(
    modelMatrix: MGMMatrix,
    private val vertexArray: MGArrayVertex,
    private val texture: MGTexture,
    private val material: MGMaterial
): MGMesh(
    modelMatrix
) {

    override fun draw() {
        super.draw()
        texture.draw()
        vertexArray.draw()
        material.draw()
    }
}