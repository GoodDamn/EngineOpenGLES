package good.damn.engine.opengl.drawers.instance

import android.opengl.GLES30
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGDrawerMeshInstanced(
    private val vertexArray: MGArrayVertexInstanced,
    private val material: MGMaterial
) {

    private var mDrawerTexture = material.textureDiffuse

    private var mode = GLES30.GL_TRIANGLES

    fun switchDrawMode(
        drawMode: MGEnumDrawMode
    ) {
        if (drawMode == MGEnumDrawMode.DIFFUSE ||
            drawMode == MGEnumDrawMode.OPAQUE
        ) {
            mDrawerTexture = material.textureDiffuse
        } else if (drawMode == MGEnumDrawMode.METALLIC) {
            mDrawerTexture = material.textureMetallic
        } else if (drawMode == MGEnumDrawMode.EMISSIVE) {
            mDrawerTexture = material.textureEmissive
        }

        mode = if (
            drawMode == MGEnumDrawMode.WIREFRAME
        ) GLES30.GL_LINES else GLES30.GL_TRIANGLES

    }

    fun drawVertices() {
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