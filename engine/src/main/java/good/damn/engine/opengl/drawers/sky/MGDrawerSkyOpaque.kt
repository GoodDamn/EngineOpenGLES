package good.damn.engine.opengl.drawers.sky

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerSkyOpaque(
    var vertexArray: MGArrayVertex,
    var texture: MGTexture
): MGIDrawer {

    override fun draw() {
        texture.draw()
        vertexArray.draw()
    }
}