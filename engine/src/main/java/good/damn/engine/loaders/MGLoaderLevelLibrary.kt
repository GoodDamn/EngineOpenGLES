package good.damn.engine.loaders

import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGProp
import good.damn.engine.models.json.MGMLevelInfoMesh
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGGeneratorMaterial
import good.damn.engine.shader.generators.MGGeneratorShader
import good.damn.engine.utils.MGUtilsFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset
import java.util.LinkedList

class MGLoaderLevelLibrary(
    private val scope: CoroutineScope,
    private val informator: MGMInformator,
    private val localPathLibTextures: String,
    localPath: String,
    localPathCullFaceList: String
) {

    companion object {
        const val ID_DIFFUSE = "textDiffuse"
        const val ID_METALLIC = "textMetallic"
        const val ID_EMISSIVE = "textEmissive"
        const val ID_OPACITY = "textOpacity"
        const val ID_NORMAL = "textNormal"
        private const val EXTENSION_TEXTURE = ".jpg"
    }

    private val mFile = MGUtilsFile.getPublicFile(
        localPath
    )

    private val mFileCullFace = MGUtilsFile.getPublicFile(
        localPathCullFaceList
    )

    private var mJson: JSONArray? = null
    private var mNonCullFaceMeshes: HashSet<String>? = null

    var isLoadLibrary = false
        private set

    var meshes: HashMap<
        String, MGProp
    >? = null
        private set

    var terrain: MGMLevelInfoMesh? = null
        private set

    fun loadLibrary(): Boolean {
        isLoadLibrary = false
        if (!mFile.exists()) {
            return false
        }

        scope.launch {
            val rootJson = fileToJson(
                mFile
            )

            mJson = rootJson.getJSONArray(
                "groups"
            ).getJSONObject(
                0
            ).getJSONArray(
                "props"
            )

            terrain = MGMLevelInfoMesh.createFromJson(
                rootJson.getJSONObject("terrain")
            )

            isLoadLibrary = true
        }

        return true
    }

    fun loadNonCullFaceList() {
        if (!mFileCullFace.exists()) {
            return
        }

        val jsonList = fileToJson(
            mFileCullFace
        ).getJSONArray(
            "meshes"
        )

        val meshesSet = HashSet<String>(
            jsonList.length()
        )

        for (i in 0 until jsonList.length()) {
            meshesSet.add(
                jsonList.getString(i)
            )
        }

        mNonCullFaceMeshes = meshesSet
    }

    fun readProp(
        mesh: MGMLevelInfoMesh,
    ): MGProp {
        val fileName = mesh.a3dMesh
        val diffuse = mesh.textures.textures[
            0
        ].diffuseMapName

        val builderMaterial = MGMaterialTexture.Builder()

        val generatorMaterial = MGGeneratorMaterial(
            informator.shaders.source
        )

        val generatorShader = MGGeneratorShader(
            informator.shaders.source
        )

        val shaderTextures = LinkedList<
            MGShaderTexture
        >()

        buildTextureMap(
            generatorMaterial,
            builderMaterial,
            "${diffuse}$EXTENSION_TEXTURE",
            1.0f,
            shaderTextures,
            MGEnumTextureType.DIFFUSE,
            ID_DIFFUSE
        )

        buildTextureMap(
            generatorMaterial,
            builderMaterial,
            "${diffuse}_m$EXTENSION_TEXTURE",
            0.0f,
            shaderTextures,
            MGEnumTextureType.METALLIC,
            ID_METALLIC
        ).run {
            if (this) {
                generatorShader.specular()
            } else {
                generatorShader.specularNo()
            }
        }

        buildTextureMap(
            generatorMaterial,
            builderMaterial,
            "${diffuse}_e$EXTENSION_TEXTURE",
            0.0f,
            shaderTextures,
            MGEnumTextureType.EMISSIVE,
            ID_EMISSIVE
        )

        buildTextureMap(
            generatorMaterial,
            builderMaterial,
            "${diffuse}_o$EXTENSION_TEXTURE",
            1.0f,
            shaderTextures,
            MGEnumTextureType.OPACITY,
            ID_OPACITY
        )

        buildTexture(
            builderMaterial,
            "${diffuse}_m$EXTENSION_TEXTURE",
            shaderTextures,
            MGEnumTextureType.NORMAL,
            ID_NORMAL
        ).run {
            if (this) {
                generatorMaterial.normalMapping(
                    ID_NORMAL
                )
            } else {
                generatorMaterial.normalVertex(
                    ID_NORMAL
                )
            }
        }

        val idMaterial = "0"

        val fragmentCodeMaterial = generatorMaterial.generate(
            idMaterial
        )

        generatorShader.lighting().material(
            fragmentCodeMaterial
        )

        val fragmentCode = generatorShader.generate(
            fragmentCodeMaterial
        )

        var cachedShader = informator.shaders.opaqueGeneratedInstanced[
            fragmentCode
        ]

        if (cachedShader == null) {
            cachedShader = MGShaderOpaque(
                MGShaderMaterial.singleMaterial(
                    shaderTextures.toTypedArray()
                )
            )
            informator.shaders.opaqueGeneratedInstanced.cacheAndCompile(
                fragmentCode,
                informator.shaders.source.verti,
                cachedShader,
                informator.glHandler,
                MGBinderAttribute.Builder()
                    .bindPosition()
                    .bindTextureCoordinates()
                    .bindNormal()
                    .bindInstancedModel()
                    .bindInstancedRotationMatrix()
                    .bindTangent()
                    .build()
            )
        }

        return MGProp(
            fileName,
            builderMaterial.build(),
            cachedShader,
            !(mNonCullFaceMeshes?.contains(
                fileName
            ) ?: false),
            LinkedList(),
        )
    }

    private fun buildTexture(
        builderMaterial: MGMaterialTexture.Builder,
        textureName: String,
        shaderTextures: MutableList<MGShaderTexture>,
        type: MGEnumTextureType,
        idUniform: String
    ): Boolean {
        if (textureExists(textureName)) {
            shaderTextures.add(
                MGShaderTexture(
                    idUniform
                )
            )

            builderMaterial.buildTexture(
                textureName,
                type
            )
            return true
        }

        return false
    }

    private fun buildTextureMap(
        generatorMaterial: MGGeneratorMaterial,
        builderMaterial: MGMaterialTexture.Builder,
        textureName: String,
        defaultValue: Float,
        shaderTextures: MutableList<MGShaderTexture>,
        type: MGEnumTextureType,
        idUniform: String
    ): Boolean {
        if (buildTexture(
            builderMaterial,
            textureName,
            shaderTextures,
            type,
            idUniform
        )) {
            generatorMaterial.mapTexture(
                idUniform,
                type
            )
            return true
        }

        generatorMaterial.mapTextureNo(
            idUniform,
            type,
            defaultValue
        )
        return false
    }

    fun readProps() {
        scope.launch {
            while (mJson == null) {}
            val json = mJson
                ?: return@launch

            val lMeshes = HashMap<
                String, MGProp
            >(json.length())

            for (i in 0 until json.length()) {
                val lJson = json.getJSONObject(i)
                val name = lJson.getString(
                    "name"
                )

                val mesh = MGMLevelInfoMesh.createFromJson(
                    lJson.getJSONObject("mesh")
                )

                lMeshes[name] = readProp(
                    mesh
                )
            }

            meshes = lMeshes
        }
    }

    private inline fun textureExists(
        textureName: String
    ) = MGUtilsFile.getPublicFile(
        "$localPathLibTextures/$textureName"
    ).exists()

    private inline fun fileToJson(
        file: File
    ) = JSONObject(
        String(
            file.inputStream().run {
                val b = readBytes()
                close()
                return@run b
            },
            Charset.forName("UTF-8")
        )
    )

}