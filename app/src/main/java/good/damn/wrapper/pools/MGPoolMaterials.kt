package good.damn.wrapper.pools

import android.util.SparseArray
import good.damn.engine.models.MGMInformator
import good.damn.engine.shader.generators.MGMMaterialShader

class MGPoolMaterials {

    private val map = SparseArray<
        MGMMaterialShader
    >()

    fun remove(
        fileNameDiffuse: String
    ) {
        map.remove(
            fileNameDiffuse.hashCode()
        )
    }

    fun loadOrGetFromCache(
        fileNameDiffuse: String,
        localPathDir: String,
        informator: MGMInformator
    ): MGMMaterialShader {
        get(fileNameDiffuse)?.run {
            return this
        }

        val materialShader = MGMMaterialShader.Builder(
            fileNameDiffuse,
            localPathDir,
            informator.shaders.source
        ).diffuse() // generate textures
            .opacity()
            .specular()
            .normal()
            .emissive(0.0f)
            .useDepthFunc()
            .build()

        // pool textures
        materialShader.materialTexture.load(
            informator.poolTextures,
            localPathDir
        )

        return materialShader
    }

    private operator fun set(
        fileNameDiffuse: String,
        material: MGMMaterialShader
    ) {
        map[
            fileNameDiffuse.hashCode()
        ] = material
    }

    private operator fun get(
        fileNameDiffuse: String
    ): MGMMaterialShader? = map[
        fileNameDiffuse.hashCode()
    ]
}