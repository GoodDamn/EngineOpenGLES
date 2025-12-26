package good.damn.engine.imports

import good.damn.engine.models.MGMInformator
import good.damn.engine.shader.generators.MGMMaterialShader
import java.io.File

class MGImportImage(
    private val informator: MGMInformator,
    private val misc: MGMImportMisc
): MGImportImplTempFile() {

    private var mIndexSubString = -1

    override fun isValidExtension(
        fileName: String
    ): Boolean {
        mIndexSubString = fileName.indexOf(
            ".jpg"
        ) - 1
        return mIndexSubString > 0
    }

    override fun onProcessTempFile(
        file: File
    ) {
        val fileNameDiffuse = file.name.substring(
            0, mIndexSubString
        )

        informator.poolMaterials[
            fileNameDiffuse
        ]?.run {
            // attach material to current edit model
            return
        }

        val folderName = file.parentFile?.run {
            "/$name"
        } ?: ""

        val localPathDir = "textures$folderName"

        // generate material
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
            localPathDir,
            informator.glLoaderTexture
        )

        // pool material
        informator.poolMaterials[
            fileNameDiffuse
        ] = materialShader

        // attach material to current edit model

    }
}