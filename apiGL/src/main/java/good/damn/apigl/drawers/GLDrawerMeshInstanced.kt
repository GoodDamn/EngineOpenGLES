package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.engine.opengl.shaders.MGShaderMaterial

class GLDrawerMeshInstanced(
    private val enableCullFace: Boolean,
    private val vertexArray: good.damn.apigl.arrays.GLArrayVertexInstanced,
    private val materials: Array<GLMaterial>
) {

    private var mode = GLES30.GL_TRIANGLES

    fun setIsWireframe(
        isWireframe: Boolean
    ) {
        mode = if (
            isWireframe
        ) GLES30.GL_LINES else GLES30.GL_TRIANGLES
    }

    fun drawVertices() {
        if (enableCullFace) {
            GLES30.glEnable(
                GLES30.GL_CULL_FACE
            )
            GLES30.glFrontFace(
                GLES30.GL_CW
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

    fun draw(
        shaderMaterial: Array<
            MGShaderMaterial
        >
    ) {
        for (i in materials.indices) {
            materials[i].draw(
                shaderMaterial[i]
            )
        }
        drawVertices()
        for (i in materials.indices) {
            materials[i].unbind(
                shaderMaterial[i]
            )
        }
    }
}