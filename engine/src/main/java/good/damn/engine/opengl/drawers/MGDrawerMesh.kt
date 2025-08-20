package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerMesh(
    vertexArray: MGArrayVertex,
    texture: MGTexture,
    material: MGMaterial,
    shader: MGIShader,
    model: MGMMatrix
): MGDrawerPositionEntity(
    shader,
    model
) {

    private val modeWireframe = MGDrawerMeshWireframe(
        vertexArray
    )

    private val modeOpaque = MGDrawerMeshOpaque(
        vertexArray,
        texture,
        material
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