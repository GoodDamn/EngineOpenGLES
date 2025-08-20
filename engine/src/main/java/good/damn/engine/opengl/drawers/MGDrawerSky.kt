package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_CCW
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerSky(
    var vertexArray: MGArrayVertex,
    var texture: MGTexture
): MGIDrawer {

    override fun draw() {
        glFrontFace(
            GL_CCW
        )
        texture.draw()
        vertexArray.draw()
    }
}