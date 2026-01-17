package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.glFrontFace
import good.damn.engine.MGRenderVars
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGIShaderNormal

open class MGDrawerMesh(
    private val vertexArray: MGDrawerVertexArray,
    private val drawEntity: MGDrawerPositionEntity,
    private val frontFace: Int = GL_CW,
): MGIDrawerShader<MGIShaderModel> {

    open fun drawNormals(
        shader: MGIShaderNormal
    ) = Unit

    override fun draw(
        shader: MGIShaderModel
    ) {
        glFrontFace(
            frontFace
        )

        drawEntity.draw(
            shader
        )

        vertexArray.draw(
            MGRenderVars.drawModeMesh.v
        )
    }
}