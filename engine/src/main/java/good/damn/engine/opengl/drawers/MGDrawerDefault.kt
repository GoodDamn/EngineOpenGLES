package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerDefault(
    var vertexArray: MGArrayVertex,
    var texture: MGTexture,
    var material: MGMaterial,
): MGIDrawer {

    override fun draw() {
        texture.draw()
        material.draw()
        vertexArray.draw()
    }

}