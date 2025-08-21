package good.damn.engine.opengl.drawers.sky

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshWireframe
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.textures.MGTexture
import javax.microedition.khronos.opengles.GL10.GL_CCW

class MGDrawerSky(
    vertexArray: MGArrayVertex,
    texture: MGTexture,
    shader: MGIShader,
    model: MGMMatrix
): MGDrawerPositionEntity(
    shader,
    model
) {
    private val modeWireframe = MGDrawerMeshWireframe(
        vertexArray
    )

    private val modeNormals = MGDrawerMeshWireframe(
        vertexArray,
        GL_TRIANGLES
    )

    private val modeOpaque = MGDrawerSkyOpaque(
        vertexArray,
        texture
    )

    private var mCurrentMode: MGIDrawer = modeOpaque

    fun switchDrawMode(
        shader: MGIShader,
        drawMode: MGEnumDrawMode
    ) {
        setDrawerShader(shader)
        mCurrentMode = when (drawMode) {
            MGEnumDrawMode.OPAQUE -> modeWireframe
            MGEnumDrawMode.WIREFRAME -> modeNormals
            MGEnumDrawMode.NORMALS -> modeOpaque
        }
    }

    override fun draw() {
        super.draw()
        glFrontFace(
            GL_CCW
        )
        mCurrentMode.draw()
    }
}