package good.damn.engine.opengl.drawers.sky

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshWireframe
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity
import good.damn.engine.opengl.drawers.MGIDrawer
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

    private val modeOpaque = MGDrawerSkyOpaque(
        vertexArray,
        texture
    )

    private var mCurrentMode: MGIDrawer = modeOpaque

    fun switchToOpaqueMode(
        shader: MGIShader
    ) {
        mCurrentMode = modeOpaque
        modeOpaque.vertexArray.changeAttrs(
            shader
        )
        setDrawerShader(
            shader
        )
    }

    fun switchToWireframeMode(
        shader: MGIShader
    ) {
        mCurrentMode = modeWireframe
        modeWireframe.vertexArray.changeAttrs(
            shader
        )
        setDrawerShader(
            shader
        )
    }

    override fun draw() {
        super.draw()
        mCurrentMode.draw()
    }
}