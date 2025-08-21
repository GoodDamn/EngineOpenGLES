package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.MGArrayVertex

class MGDrawerVertexArray(
    override var vertexArray: MGArrayVertex,
    private var drawMethod: Int = GLES30.GL_LINES
): MGIDrawerMesh {

    override fun draw() {
        vertexArray.draw(
            drawMethod
        )
    }

}