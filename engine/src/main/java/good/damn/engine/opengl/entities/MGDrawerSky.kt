package good.damn.engine.opengl.entities

import android.opengl.GLES30.GL_CCW
import android.opengl.GLES30.GL_LINES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.MGEngine
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerSky(
    shader: MGIShader,
    modelMatrix: MGMMatrix,
    var vertexArray: MGArrayVertex,
    var texture: MGTexture
): MGMesh(
    shader,
    modelMatrix
) {

    init {
        modelMatrix.setScale(
            200000f,
            200000f,
            200000f
        )
    }

    override fun draw() {
        glFrontFace(
            GL_CCW
        )
        super.draw()

        if (MGEngine.isWireframe) {
            vertexArray.draw(
                GL_LINES
            )
            return
        }

        texture.draw()
        vertexArray.draw()
    }
}