package good.damn.engine.opengl.drawers.instance

import android.opengl.GLES30
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGDrawerMeshInstanced(
    private val enableCullFace: Boolean,
    private val vertexArray: MGArrayVertexInstanced,
    private val material: MGMaterial
) {

    private var mDrawerTexture = material.textureDiffuse

    private var mode = GLES30.GL_TRIANGLES

    fun switchDrawMode(
        drawMode: MGEnumDrawMode
    ) {
        mDrawerTexture = when (
            drawMode
        ) {
            MGEnumDrawMode.METALLIC -> material.textureDiffuse
            MGEnumDrawMode.EMISSIVE -> material.textureEmissive
            MGEnumDrawMode.NORMAL_MAP -> material.textureNormal
            else -> material.textureDiffuse
        }

        mode = if (
            drawMode == MGEnumDrawMode.WIREFRAME
        ) GLES30.GL_LINES else GLES30.GL_TRIANGLES

    }

    fun drawVertices() {
        if (enableCullFace) {
            GLES30.glEnable(
                GLES30.GL_CULL_FACE
            )
        } else {
            GLES30.glDisable(
                GLES30.GL_CULL_FACE
            )
        }

        vertexArray.drawInstanced(
            mode
        )
    }

    fun drawSingleTexture(
        shaderTexture: MGIShaderTextureUniform
    ) {
        mDrawerTexture.draw(
            shaderTexture
        )
        drawVertices()
        mDrawerTexture.unbind(
            shaderTexture
        )
    }

    fun draw(
        shaderMaterial: MGShaderMaterial
    ) {
        material.draw(
            shaderMaterial
        )
        drawVertices()
        material.unbind(
            shaderMaterial
        )
    }
}