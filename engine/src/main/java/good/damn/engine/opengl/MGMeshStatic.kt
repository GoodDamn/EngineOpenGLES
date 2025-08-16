package good.damn.engine.opengl

import android.opengl.GLES30.*
import good.damn.engine.MGEngine
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMesh
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsBuffer

open class MGMeshStatic(
    obj: MGObject3D,
    texturePath: String,
    program: Int
): MGMesh() {

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

    fun invalidatePosition() {
        model[12] = x
        model[13] = y
        model[14] = z
    }

    override fun draw(
        camera: MGCamera
    ) {
        glFrontFace(
            GL_CW
        )
        super.draw(
            camera
        )

        if (MGEngine.isWireframe) {
            mVertexArray.draw(
                GL_LINES
            )
            return
        }

        mTexture.draw()
        material.draw()
        mVertexArray.draw()
    }
}