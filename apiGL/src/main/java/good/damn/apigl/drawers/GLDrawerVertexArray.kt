package good.damn.apigl.drawers

import android.opengl.GLES30.glBindVertexArray
import android.opengl.GLES30.glDrawElements
import good.damn.apigl.arrays.GLArrayVertexConfigurator

class GLDrawerVertexArray(
    private val configurator: GLArrayVertexConfigurator
): GLIDrawerMesh {

    override fun draw(
        method: Int
    ) {
        glBindVertexArray(
            configurator.vertexArrayId
        )

        glDrawElements(
            method,
            configurator.indicesCount,
            configurator.config.type,
            0
        )

        glBindVertexArray(0)
    }

}