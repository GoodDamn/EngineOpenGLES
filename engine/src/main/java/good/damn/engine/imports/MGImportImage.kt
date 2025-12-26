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

        informator.poolMaterials[
            fileNameDiffuse
        ]?.run {
            processShader(
                this
            )
            return
        }

        val localPathDir = "textures/$fileNameDiffuse"

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

        // generate shader
        processShader(
            materialShader
        )
    }

    private inline fun processShader(
        materialShader: MGMMaterialShader
    ) {
        informator.shaders.cacheGeometryPass[
            materialShader.srcCodeMaterial
        ]?.run {
            attachMaterial(
                this,
                materialShader
            )
            return
        }

        // Compile shader
        val shader = MGShaderGeometryPassModel(
            arrayOf(
                MGShaderMaterial(
                    materialShader.shaderTextures
                )
            )
        )

        informator.shaders.cacheGeometryPass.cacheAndCompile(
            materialShader.srcCodeMaterial,
            informator.shaders.source.vert,
            shader,
            informator.glHandler,
            MGBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .bindNormal()
                .build()
        )

        attachMaterial(
            shader,
            materialShader
        )
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