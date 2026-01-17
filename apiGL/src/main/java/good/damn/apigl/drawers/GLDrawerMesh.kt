package good.damn.apigl.drawers

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.glFrontFace
import good.damn.apigl.GLRenderVars
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLIShaderNormal

open class GLDrawerMesh(
    private val vertexArray: GLDrawerVertexArray,
    private val drawEntity: GLDrawerPositionEntity,
    private val frontFace: Int = GL_CW,
): GLIDrawerShader<GLIShaderModel> {

    open fun drawNormals(
        shader: GLIShaderNormal
    ) = Unit

    override fun draw(
        shader: GLIShaderModel
    ) {
        glFrontFace(
            frontFace
        )

        drawEntity.draw(
            shader
        )

        vertexArray.draw(
            GLRenderVars.drawModeMesh.v
        )
    }
}