package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerTexture(
    private val drawerMaterial: MGIDrawerTexture,
    private val textureDiffuse: MGTexture,
    private val textureMetallic: MGTexture,
    private val textureEmissive: MGTexture,
    private val drawerModeSwitch: MGDrawerMeshSwitch
): MGIDrawer {

    private var drawerTexture = drawerMaterial

    private val mInitShaderDiffuse = textureDiffuse.shader
    private val mInitShaderMetallic = textureMetallic.shader
    private val mInitShaderEmissive = textureEmissive.shader

    fun switchDrawMode(
        drawMode: MGEnumDrawMode,
        shaderTexture: MGIShaderTextureUniform?
    ) {
        drawerTexture = when (
            drawMode
        ) {
            MGEnumDrawMode.DIFFUSE -> textureDiffuse.apply {
                shader = shaderTexture!!
            }
            MGEnumDrawMode.METALLIC -> textureMetallic.apply {
                shader = shaderTexture!!
            }
            MGEnumDrawMode.EMISSIVE -> textureEmissive.apply {
                shader = shaderTexture!!
            }
            else -> drawerMaterial.apply {
                textureDiffuse.shader = mInitShaderDiffuse
                textureMetallic.shader = mInitShaderMetallic
                textureEmissive.shader = mInitShaderEmissive
            }
        }
    }

    override fun draw() {
        drawerTexture.draw()
        drawerModeSwitch.draw()
        drawerTexture.draw()
    }
}