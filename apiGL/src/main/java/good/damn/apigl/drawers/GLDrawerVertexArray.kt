package good.damn.apigl.drawers

import android.opengl.GLES30.glBindVertexArray
import android.opengl.GLES30.glDrawElements

class GLDrawerVertexArray(
    private val configurator: good.damn.apigl.arrays.GLArrayVertexConfigurator
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