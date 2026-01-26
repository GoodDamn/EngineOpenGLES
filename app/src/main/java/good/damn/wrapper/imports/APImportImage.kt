package good.damn.wrapper.imports

import good.damn.apigl.drawers.GLDrawerMaterialTexture
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.models.MGMParameters
import good.damn.engine2.opengl.pools.MGMPools
import good.damn.engine2.shader.generators.MGMMaterialShader
import good.damn.wrapper.models.APMUserContent
import java.io.File

class APImportImage(
    private val pools: MGMPools,
    private val shaders: MGMInformatorShader,
    private val parameters: MGMParameters
): APIImport {

    private val mBinderAttribute = GLBinderAttribute.Builder()
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

    override fun processUserContent(
        userContent: APMUserContent
    ) {
        val fileNameDiffuse = userContent.fileName.substring(
            0, mIndexSubString
        )

        val material = pools.materials.loadOrGetFromCache(
            fileNameDiffuse,
            "textures/$fileNameDiffuse"
        )

        processShader(
            material
        )
    }

    private inline fun processShader(
        materialShader: MGMMaterialShader
    ) {
        val shader = shaders.cacheGeometryPass.loadOrGetFromCache(
            materialShader.srcCodeMaterial,
            shaders.source.vert,
            mBinderAttribute,
            arrayOf(
                GLShaderMaterial(
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
        shader: GLShaderGeometryPassModel,
        materialShader: MGMMaterialShader
    ) {
        // attach material to model
        parameters.currentEditMesh?.apply {
            drawer.drawer.material = arrayOf(
                GLMaterial(
                    GLDrawerMaterialTexture(
                        materialShader.textures
                    )
                )
            )
            this.shader = shader
        }
    }
}