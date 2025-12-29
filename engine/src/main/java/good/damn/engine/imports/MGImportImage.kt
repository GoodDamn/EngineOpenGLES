package good.damn.engine.imports

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGMMaterialShader
import java.io.File

class MGImportImage(
    private val informator: MGMInformator,
    private val misc: MGMImportMisc
): MGImportImplTempFile() {

    private val mBinderAttribute = MGBinderAttribute.Builder()
        .bindPosition()
        .bindTextureCoordinates()
        .bindNormal()
        .build()

    private var mIndexSubString = -1

    override fun isValidExtension(
        fileName: String
    ): Boolean {
        mIndexSubString = fileName.indexOf(
            ".jpg"
        )
        return mIndexSubString > 0
    }

    override fun onProcessTempFile(
        file: File
    ) {
        val fileNameDiffuse = file.name.substring(
            0, mIndexSubString
        )

        val material = informator.poolMaterials.loadOrGetFromCache(
            fileNameDiffuse,
            "textures/$fileNameDiffuse",
            informator
        )

        processShader(
            material
        )
    }

    private inline fun processShader(
        materialShader: MGMMaterialShader
    ) {
        val shader = informator.shaders.cacheGeometryPass.loadOrGetFromCache(
            materialShader.srcCodeMaterial,
            informator.shaders.source.vert,
            mBinderAttribute,
            arrayOf(
                MGShaderMaterial(
                    materialShader.shaderTextures
                )
            )
        )

        attachMaterial(
            shader,
            materialShader
        )

        // Compile shader
        /*val shader = MGShaderGeometryPassModel(
            arrayOf(
                MGShaderMaterial(
                    materialShader.shaderTextures
                )
            )
        )*/
    }

    private inline fun attachMaterial(
        shader: MGShaderGeometryPassModel,
        materialShader: MGMMaterialShader
    ) {
        // attach material to model
        informator.currentEditMesh?.apply {
            drawer.material = arrayOf(
                MGMaterial(
                    materialShader.materialTexture
                )
            )
            this.shader = shader
        }
    }
}