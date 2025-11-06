package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_LINES
import android.opengl.GLES30.GL_TRIANGLES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.enums.MGEnumDrawMode
import good.damn.engine.opengl.matrices.MGMatrixNormal
import good.damn.engine.opengl.models.MGMDrawMode
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
): MGIDrawer {

    private var modeVertex = GL_TRIANGLES

    open fun switchDrawMode(
        shader: MGIShaderNormal?
    ) = Unit

    fun switchDrawMode(
        shader: MGIShaderModel,
        drawMode: MGEnumDrawMode
    ) {
        drawEntity.shader = shader
        modeVertex = when (
            drawMode
        ) {
            MGEnumDrawMode.WIREFRAME -> GL_LINES
            else -> GL_TRIANGLES
        }
    }

    override fun draw() {
        glFrontFace(
            frontFace
        )
        drawEntity.draw()
        vertexArray.draw(
            modeVertex
        )
    }
}