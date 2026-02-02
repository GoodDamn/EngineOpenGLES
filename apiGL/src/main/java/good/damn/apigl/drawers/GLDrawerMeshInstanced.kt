package good.damn.apigl.drawers

import android.opengl.GLES30
import good.damn.apigl.GLRenderVars
import good.damn.apigl.shaders.GLShaderMaterial

class GLDrawerMeshInstanced(
    private val enableCullFace: Boolean,
    private val vertexArray: good.damn.apigl.arrays.GLArrayVertexInstanced,
    private val materials: Array<GLMaterial>
) {

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
            GLRenderVars.drawModeMesh.v
        )
    }

    fun draw(
        shaderMaterial: Array<
            GLShaderMaterial
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