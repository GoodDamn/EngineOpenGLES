package good.damn.engine.opengl.drawers.sky

import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.drawers.MGDrawerMeshOpaque
import good.damn.engine.opengl.drawers.MGDrawerMeshWireframe
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerSky(
    vertexArray: MGArrayVertex,
    texture: MGTexture
): MGIDrawer {
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
    }

    fun switchToWireframeMode(
        shader: MGIShader
    ) {
        mCurrentMode = modeWireframe
        modeWireframe.vertexArray.changeAttrs(
            shader
        )
    }

    override fun draw() {
        mCurrentMode.draw()
    }
}