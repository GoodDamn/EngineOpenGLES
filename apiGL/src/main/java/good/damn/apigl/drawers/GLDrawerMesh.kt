package good.damn.apigl.drawers

import android.opengl.GLES30.glFrontFace
import good.damn.apigl.GLRenderVars
import good.damn.apigl.enums.GLEnumFaceOrder
import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLIShaderNormal

data class GLDrawerMesh(
    var vertexArray: GLDrawerVertexArray,
    var drawEntity: GLDrawerPositionEntity,
    var frontFace: GLEnumFaceOrder
) {

    companion object {
        @JvmStatic
        fun draw(
            drawer: GLDrawerMesh,
            shader: GLIShaderModel
        ) {
            glFrontFace(
                drawer.frontFace.v
            )

            drawer.drawEntity.draw(
                shader
            )

            drawer.vertexArray.draw(
                GLRenderVars.drawModeMesh.v
            )
        }
    }
}