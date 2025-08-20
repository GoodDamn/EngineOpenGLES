package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.MGArrayVertex

class MGDrawerWireframe(
    var vertexArray: MGArrayVertex
): MGIDrawer {

    override fun draw() {
        vertexArray.draw(
            GLES30.GL_LINES
        )
    }

}