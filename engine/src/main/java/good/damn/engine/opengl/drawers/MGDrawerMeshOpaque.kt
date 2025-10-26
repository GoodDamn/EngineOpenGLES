package good.damn.engine.opengl.drawers

import android.opengl.GLES30.*
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.textures.MGTexture

class MGDrawerMeshOpaque(
    override var vertexArray: MGArrayVertex,
    var material: MGMaterial,
): MGIDrawerMesh {

    override fun draw() {
        material.draw()
        vertexArray.draw()
        material.unbind()
    }

}