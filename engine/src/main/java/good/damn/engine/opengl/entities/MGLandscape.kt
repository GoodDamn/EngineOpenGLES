package good.damn.engine.opengl.entities

import android.opengl.GLES30.*
import android.util.Log
import good.damn.engine.MGEngine
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGVector
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.camera.MGMMatrix
import good.damn.engine.opengl.drawers.MGIDrawer
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.shaders.MGIShader
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBuffer
import java.nio.FloatBuffer

class MGLandscape(
    var drawer: MGIDrawer,
    modelMatrix: MGMMatrix,
    shader: MGIShader
): MGMesh(
    shader,
    modelMatrix
) {

    override fun draw() {
        glFrontFace(
            GL_CW
        )

        super.draw()
        drawer.draw()
    }

    fun intersect(
        position: MGVector,
        direction: MGVector,
        outResult: MGVector
    ) {
        outResult.x = position.x + direction.x * 10f
        outResult.y = position.y + direction.y * 10f
        outResult.z = position.z + direction.z * 10f
    }
}