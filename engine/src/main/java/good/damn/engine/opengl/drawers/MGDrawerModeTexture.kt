package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.textures.MGTexture

class MGDrawerModeTexture(
    val texture: MGTexture,
    private val vertexArray: MGDrawerVertexArray
): MGIDrawer {

    override fun draw() {
        texture.draw()
        vertexArray.draw()
        texture.unbind()
    }
}