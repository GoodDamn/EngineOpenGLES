package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.enums.MGEnumDrawMode

class MGDrawerModeSwitch(
    vertexArray: MGArrayVertex,
    private val modeOpaque: MGIDrawer,
    private val frontFace: Int = GL_CW
): MGIDrawer {

    private val modeLines = MGDrawerVertexArray(
        vertexArray
    )

    private val modeTriangles = MGDrawerVertexArray(
        vertexArray,
        GL_TRIANGLES
    )

    private var mCurrentMode: MGIDrawer = modeOpaque

    fun switchDrawMode(
        drawMode: MGEnumDrawMode
    ) {
        mCurrentMode = when (drawMode) {
            MGEnumDrawMode.OPAQUE -> modeLines
            MGEnumDrawMode.WIREFRAME -> modeTriangles
            MGEnumDrawMode.NORMALS -> modeTriangles
            MGEnumDrawMode.TEX_COORDS -> modeOpaque
        }
    }

    override fun draw() {
        glFrontFace(
            frontFace
        )
        mCurrentMode.draw()
    }

}