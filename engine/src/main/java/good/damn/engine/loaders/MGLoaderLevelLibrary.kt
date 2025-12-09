package good.damn.engine.loaders

import android.util.Log
import good.damn.engine.models.MGMGeneratorMaterial
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGProp
import good.damn.engine.models.json.MGMLevelInfoMesh
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.textures.MGTextureActive
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
        const val ID_BLEND = "textBlend"
        const val ID_AMBIENT_OCCLUSION = "textAmbientOcclusion"
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
        mesh: MGMLevelInfoMesh
    ): MGProp {
        val fileName = mesh.a3dMesh
        val textures = mesh.textures.textures

        /*val generatorShader = MGGeneratorShader(
            informator.shaders.source
        )

        val shaderMaterials = LinkedList<MGShaderMaterial>()
        val codeMaterials = LinkedList<MGMGeneratorMaterial>()

        val buildersMaterial = LinkedList<MGMaterialTexture>()

        val texturesCount = 7
        var indexMaterial = 0
        for (index in textures.indices) {
            val diffuse = textures[index].diffuseMapName
            val controlMapName = textures[index].controlMapName

            val generatorMaterial = MGGeneratorMaterial(
                informator.shaders.source
            )

            val builderMaterial = MGMaterialTexture.Builder()

            val shaderTextures = LinkedList<
                MGShaderTexture
            >()

            val texCoordScale = if (
                controlMapName == null
            ) 1f else 105f


            buildTextureMap(
                generatorMaterial,
                builderMaterial,
                "${diffuse}$EXTENSION_TEXTURE",
                1.0f,
                shaderTextures,
                MGEnumTextureType.DIFFUSE,
                indexMaterial,
                "${ID_DIFFUSE}$index",
                texCoordScale
            )

            buildTextureMap(
                generatorMaterial,
                builderMaterial,
                "${diffuse}_m$EXTENSION_TEXTURE",
                0.0f,
                shaderTextures,
                MGEnumTextureType.METALLIC,
                indexMaterial,
                "${ID_METALLIC}$index",
                texCoordScale
            ).run {
                if (index == 0) {
                    if (this) {
                        generatorShader.specular()
                    } else {
                        generatorShader.specularNo()
                    }
                }
            }

            buildTextureMap(
                generatorMaterial,
                builderMaterial,
                "${diffuse}_e$EXTENSION_TEXTURE",
                0.0f,
                shaderTextures,
                MGEnumTextureType.EMISSIVE,
                indexMaterial,
                "${ID_EMISSIVE}$index",
                texCoordScale
            )

            buildTextureMap(
                generatorMaterial,
                builderMaterial,
                "${diffuse}_o$EXTENSION_TEXTURE",
                1.0f,
                shaderTextures,
                MGEnumTextureType.OPACITY,
                indexMaterial,
                "${ID_OPACITY}$index",
                texCoordScale
            )

            buildTexture(
                builderMaterial,
                "${diffuse}_n$EXTENSION_TEXTURE",
                shaderTextures,
                MGEnumTextureType.NORMAL,
                indexMaterial,
                "${ID_NORMAL}$index"
            ).run {
                if (this) {
                    generatorMaterial.normalMapping(
                        "${ID_NORMAL}$index",
                        texCoordScale
                    )
                } else {
                    generatorMaterial.normalVertex(
                        "${ID_NORMAL}$index"
                    )
                }
            }

            buildTextureMap(
                generatorMaterial,
                builderMaterial,
                "${controlMapName}$EXTENSION_TEXTURE",
                1.0f,
                shaderTextures,
                MGEnumTextureType.BLEND,
                indexMaterial,
                "${ID_BLEND}$index",
                1f
            )

            buildTextureMap(
                generatorMaterial,
                builderMaterial,
                "${mesh.ambientOcclusionMapName}$EXTENSION_TEXTURE",
                1.0f,
                shaderTextures,
                MGEnumTextureType.AMBIENT_OCCLUSION,
                indexMaterial,
                "${ID_AMBIENT_OCCLUSION}$index",
                1.0f
            )


            val fragmentCodeMaterial = generatorMaterial.generate(
                index.toString()
            )

            codeMaterials.add(
                fragmentCodeMaterial
            )

            if (index == 0) {
                generatorShader.lighting()
            }

            generatorShader.material(
                fragmentCodeMaterial
            )

            shaderMaterials.add(
                MGShaderMaterial(
                    "shine",
                    shaderTextures.toTypedArray()
                )
            )

            buildersMaterial.add(
                builderMaterial.build()
            )
            indexMaterial += texturesCount

            if (controlMapName == null) {
                break
            }
        }*/

        val texture = textures[0]

        /*val fragmentCode = generatorShader.generate(
            codeMaterials.toTypedArray()
        )

        var cachedShader = informator.shaders.opaqueGeneratedInstanced[
            fragmentCode
        ]

        if (cachedShader == null) {
            cachedShader = MGShaderOpaque(
                shaderMaterials.toTypedArray()
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
        }*/

        return MGProp(
            fileName,
            arrayOf(
                MGMaterialTexture.Builder()
                    .buildTexture(
                        "${texture.diffuseMapName}$EXTENSION_TEXTURE",
                        MGEnumTextureType.DIFFUSE
                    ).buildTexture(
                        "${texture.diffuseMapName}_m$EXTENSION_TEXTURE",
                        MGEnumTextureType.METALLIC
                    ).build()
            ),
            informator.shaders.geometryPass,
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
        indexMaterial: Int,
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
                type,
                MGTextureActive(
                    indexMaterial + type.v
                )
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
        indexMaterial: Int,
        idUniform: String,
        texCoordScale: Float
    ): Boolean {
        if (buildTexture(
            builderMaterial,
            textureName,
            shaderTextures,
            type,
            indexMaterial,
            idUniform
        )) {
            generatorMaterial.mapTexture(
                idUniform,
                type,
                texCoordScale
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