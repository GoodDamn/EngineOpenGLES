package good.damn.engine.opengl.pools

import android.util.SparseArray
import good.damn.engine.opengl.models.MGMPoolVertexArray

class MGPoolMeshesStatic {

    private val map = SparseArray<
        Array<MGMPoolVertexArray>
    >()

    fun remove(
        fileNameModel: String
    ) {
        map.remove(
            fileNameModel.hashCode()
        )
    }

    operator fun set(
        fileNameModel: String,
        arrayVertex: Array<MGMPoolVertexArray>
    ) {
        map[
            fileNameModel.hashCode()
        ] = arrayVertex
    }

    operator fun get(
        fileNameModel: String
    ) = map[
        fileNameModel.hashCode()
    ]
}