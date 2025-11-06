package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerMeshTexture(
    private val drawerTexture: MGDrawerTexture,
    private val drawerSwitch: MGDrawerMeshSwitch
): MGIDrawer {

    private var mCurrentDrawer: MGIDrawer = drawerTexture

    fun switchDrawMode(
        shader: MGIShaderNormal?
    ) = drawerSwitch.switchDrawMode(
        shader
    )

    fun switchDrawMode(
        shader: MGIShaderModel,
        shaderTexture: MGIShaderTextureUniform?,
        drawMode: MGEnumDrawMode
    ) {
        mCurrentDrawer = when (drawMode) {
            MGEnumDrawMode.WIREFRAME,
            MGEnumDrawMode.NORMALS,
            MGEnumDrawMode.TEX_COORDS -> drawerSwitch
            else -> drawerTexture.apply {
                switchDrawMode(
                    drawMode,
                    shaderTexture
                )
            }
        }

        drawerSwitch.switchDrawMode(
            shader,
            drawMode
        )
    }

    override fun draw() {
        mCurrentDrawer.draw()
    }

}