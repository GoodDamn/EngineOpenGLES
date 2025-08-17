package good.damn.engine.opengl.entities

import android.opengl.GLES30.GL_CW
import android.opengl.GLES30.GL_LINES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.MGEngine
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3D
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.textures.MGTexture

class MGMeshStatic(
    shader: MGIShader,
    modelMatrix: MGMMatrix,
    var material: MGMaterial,
    var texture: MGTexture,
    var vertexArray: MGArrayVertex
): MGMesh(
    shader,
    modelMatrix
) {

    override fun draw() {
        glFrontFace(
            GL_CW
        )

        super.draw()

        if (MGEngine.isWireframe) {
            vertexArray.draw(
                GL_LINES
            )
            return
        }

        vertexArray.draw()
        texture.draw()
        material.draw()
    }
}