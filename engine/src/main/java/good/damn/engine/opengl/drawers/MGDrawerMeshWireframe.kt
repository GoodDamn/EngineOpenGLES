package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.MGArrayVertex

class MGDrawerMeshWireframe(
    override var vertexArray: MGArrayVertex
): MGIDrawerMesh {

    override fun draw() {
        vertexArray.draw(
            GLES30.GL_LINES
        )
    }

}