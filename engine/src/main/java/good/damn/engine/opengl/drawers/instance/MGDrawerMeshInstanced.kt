package good.damn.engine.opengl.drawers.instance

import android.opengl.GLES30
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGDrawerMeshInstanced(
    private val enableCullFace: Boolean,
    private val vertexArray: MGArrayVertexInstanced,
    private val materials: Array<MGMaterial>
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