package good.damn.engine.opengl.pools

import android.util.SparseArray
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.models.MGMPoolVertexArray
import good.damn.engine.opengl.models.MGMShaderMaterialModel

class MGPoolMaterials {

    lateinit var default: MGMShaderMaterialModel
        private set

    private val map = SparseArray<
        MGMShaderMaterialModel
    >()

    fun configureDefault(
        informator: MGMInformator
    ) {
        default = MGMaterial.generateShaderAndMaterial(
            null,
            informator
        )
    }

    fun remove(
        fileNameDiffuse: String
    ) {
        map.remove(
            fileNameDiffuse.hashCode()
        )
    }

    operator fun set(
        fileNameDiffuse: String,
        material: MGMShaderMaterialModel
    ) {
        map[
            fileNameDiffuse.hashCode()
        ] = material
    }

    operator fun get(
        fileNameDiffuse: String
    ): MGMShaderMaterialModel? = map[
        fileNameDiffuse.hashCode()
    ]
}