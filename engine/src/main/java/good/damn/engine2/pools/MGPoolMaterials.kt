package good.damn.engine2.pools

import android.util.SparseArray
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.shader.generators.MGMMaterialShader

class MGPoolMaterials(
    private val poolTextures: MGPoolTextures,
    private val shaders: MGMInformatorShader
) {

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
        localPathDir: String
    ): MGMMaterialShader {
        get(fileNameDiffuse)?.run {
            return this
        }

        val materialShader = MGMMaterialShader.Builder(
            fileNameDiffuse,
            localPathDir,
            shaders.source
        ).diffuse() // generate textures
            .opacity()
            .specular()
            .normal()
            .emissive(0.0f)
            .useDepthFunc()
            .build()

        materialShader.loadTextures(
            poolTextures,
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