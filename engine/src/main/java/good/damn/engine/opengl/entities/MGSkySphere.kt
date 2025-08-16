package good.damn.engine.opengl.entities

import android.opengl.GLES30.GL_CCW
import android.opengl.GLES30.GL_LINES
import android.opengl.GLES30.glFrontFace
import good.damn.engine.MGEngine
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3D
import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.textures.MGTexture

class MGSkySphere(
    program: Int
): MGMesh() {

    private val material = MGMaterial(
        program
    )

    private val mTexture = MGTexture(
        "textures/sky/skysphere_light.jpg",
        program
    )

    private val mVertexArray = MGArrayVertex().apply {
        val obj = MGObject3D.createFromAssets(
            "objs/semi_sphere.obj"
        )
        configure(
            program,
            obj.vertices,
            obj.indices
        )
    }

    init {
        setScale(
            200000f,
            200000f,
            200000f
        )
    }

    override fun draw(
        camera: MGCamera
    ) {
        glFrontFace(
            GL_CCW
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