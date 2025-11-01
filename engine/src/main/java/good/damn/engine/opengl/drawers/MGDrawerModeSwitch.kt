package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform

class MGDrawerModeSwitch(
    vertexArray: MGArrayVertex,
    private val modeOpaque: MGIDrawer,
    private val modeTextureDiffuse: MGDrawerModeTexture,
    private val modeTextureMetallic: MGDrawerModeTexture,
    private val modeTextureEmissive: MGDrawerModeTexture,
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

    private val mInitShaderDiffuse = modeTextureDiffuse.texture.shader
    private val mInitShaderMetallic = modeTextureMetallic.texture.shader
    private val mInitShaderEmissive = modeTextureEmissive.texture.shader

    fun switchDrawMode(
        drawMode: MGEnumDrawMode,
        shaderTexture: MGIShaderTextureUniform?
    ) {
        mCurrentMode = when (drawMode) {
            MGEnumDrawMode.OPAQUE -> modeOpaque.apply {
                modeTextureDiffuse.texture.shader = mInitShaderDiffuse
                modeTextureMetallic.texture.shader = mInitShaderMetallic
                modeTextureEmissive.texture.shader = mInitShaderEmissive
            }
            MGEnumDrawMode.WIREFRAME -> modeLines
            MGEnumDrawMode.NORMALS -> modeTriangles
            MGEnumDrawMode.TEX_COORDS -> modeTriangles
            MGEnumDrawMode.DIFFUSE -> modeTextureDiffuse.apply {
                texture.shader = shaderTexture!!
            }
            MGEnumDrawMode.METALLIC -> modeTextureMetallic.apply {
                texture.shader = shaderTexture!!
            }
            MGEnumDrawMode.EMISSIVE -> modeTextureEmissive.apply {
                texture.shader = shaderTexture!!
            }
        }
    }

    override fun draw() {
        glFrontFace(
            frontFace
        )
        mCurrentMode.draw()
    }

}