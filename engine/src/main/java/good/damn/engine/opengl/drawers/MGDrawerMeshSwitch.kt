package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_LINES
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.shaders.MGIShaderCamera
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.shaders.MGIShaderTexture
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform

open class MGDrawerMeshSwitch(
    private val vertexArray: MGArrayVertex,
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