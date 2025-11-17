package good.damn.engine.opengl.drawers

import android.opengl.GLES30
import android.opengl.GLES30.GL_UNSIGNED_INT
import android.opengl.GLES30.glBindVertexArray
import android.opengl.GLES30.glDrawElements
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import java.nio.IntBuffer

class MGDrawerVertexArray(
    private val configurator: MGArrayVertexConfigurator
): MGIDrawerMesh {

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