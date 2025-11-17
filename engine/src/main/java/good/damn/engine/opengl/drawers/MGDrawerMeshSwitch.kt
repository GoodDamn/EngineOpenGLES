package good.damn.engine.opengl.drawers

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_LINES
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGIShaderNormal

open class MGDrawerMeshSwitch(
    private val vertexArray: MGDrawerVertexArray,
    private val drawEntity: MGDrawerPositionEntity,
    private val frontFace: Int = GL_CW,
): MGIDrawerShader<MGIShaderModel> {

    private var modeVertex = GL_TRIANGLES

    fun switchDrawMode(
        drawMode: MGEnumDrawMode
    ) {
        modeVertex = when (
            drawMode
        ) {
            MGEnumDrawMode.WIREFRAME -> GL_LINES
            else -> GL_TRIANGLES
        }
    }

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
            modeVertex
        )
    }
}