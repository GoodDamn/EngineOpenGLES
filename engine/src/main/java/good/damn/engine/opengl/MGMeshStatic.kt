package good.damn.engine.opengl

import android.opengl.GLES30.*
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBuffer

open class MGMeshStatic(
    obj: MGObject3D,
    texturePath: String,
    program: Int
): MGMesh(
    program
) {

    var material = MGMaterial(
        program
    )

    private val mTexture = MGTexture(
        texturePath,
        program
    )

    private val mVertexArray = MGArrayVertex().apply {
        configure(
            program,
            obj.vertices,
            obj.indices
        )
    }

    override fun draw(
        camera: MGCamera
    ) {
        super.draw(
            camera
        )

        mTexture.draw()
        material.draw()
        mVertexArray.draw()
    }
}