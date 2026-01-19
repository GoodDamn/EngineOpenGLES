package good.damn.apigl.drawers

import android.opengl.GLES30.glFrontFace
import good.damn.apigl.GLRenderVars
import good.damn.apigl.enums.GLEnumFaceOrder
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLIShaderNormal

open class GLDrawerMesh(
    private val vertexArray: GLDrawerVertexArray,
    private val drawEntity: GLDrawerPositionEntity,
    private val frontFace: GLEnumFaceOrder,
): GLIDrawerShader<GLIShaderModel> {

    open fun drawNormals(
        shader: GLIShaderNormal
    ) = Unit

    override fun draw(
        shader: GLIShaderModel
    ) {
        glFrontFace(
            frontFace.v
        )

        drawEntity.draw(
            shader
        )

        vertexArray.draw(
            GLRenderVars.drawModeMesh.v
        )
    }
}