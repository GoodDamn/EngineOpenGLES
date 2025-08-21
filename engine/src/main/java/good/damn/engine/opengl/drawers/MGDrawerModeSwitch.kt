package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerModeSwitch(
    vertexArray: MGArrayVertex,
    private val modeOpaque: MGIDrawer,
    private val frontFace: Int = GL_CW
): MGIDrawer {

    private val modeWireframe = MGDrawerMeshWireframe(
        vertexArray
    )

    private val modeNormals = MGDrawerMeshWireframe(
        vertexArray,
        GL_TRIANGLES
    )

    private var mCurrentMode: MGIDrawer = modeOpaque

    fun switchDrawMode(
        drawMode: MGEnumDrawMode
    ) {
        mCurrentMode = when (drawMode) {
            MGEnumDrawMode.OPAQUE -> modeWireframe
            MGEnumDrawMode.WIREFRAME -> modeNormals
            MGEnumDrawMode.NORMALS -> modeOpaque
        }
    }

    override fun draw() {
        glFrontFace(
            frontFace
        )
        mCurrentMode.draw()
    }

}