package good.damn.engine.opengl.pools

import android.util.SparseArray
import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.shader.generators.MGMMaterialShader

class MGPoolMaterials {

    lateinit var default: MGMMaterialShader
        private set

    private val map = SparseArray<
        MGMMaterialShader
    >()

    fun configureDefault(
        informator: MGMInformator
    ) {
        val materialShader = MGMMaterialShader.Builder(
            "",
            "",
            informator.shaders.source
        ).diffuse().opacity()
            .emissive(0.0f)
            .normal()
            .useDepthFunc()
            .specular()
            .build()

        default = materialShader
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
        material: MGMMaterialShader
    ) {
        map[
            fileNameDiffuse.hashCode()
        ] = material
    }

    operator fun get(
        fileNameDiffuse: String
    ): MGMMaterialShader? = map[
        fileNameDiffuse.hashCode()
    ]
}