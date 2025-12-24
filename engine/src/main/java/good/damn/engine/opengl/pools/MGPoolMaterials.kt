package good.damn.engine.opengl.pools

import android.util.SparseArray
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.models.MGMPoolVertexArray

class MGPoolMaterials {
    private val map = SparseArray<
        MGMaterial
    >()

    fun remove(
        fileNameDiffuse: String
    ) {
        map.remove(
            fileNameDiffuse.hashCode()
        )
    }

    operator fun set(
        fileNameDiffuse: String,
        material: MGMaterial
    ) {
        map[
            fileNameDiffuse.hashCode()
        ] = material
    }

    operator fun get(
        fileNameDiffuse: String
    ): MGMaterial? = map[
        fileNameDiffuse.hashCode()
    ]
}