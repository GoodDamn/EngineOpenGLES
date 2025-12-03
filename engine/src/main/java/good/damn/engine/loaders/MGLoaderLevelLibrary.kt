package good.damn.engine.loaders

import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInformatorShader
import good.damn.engine.models.MGProp
import good.damn.engine.models.json.MGMLevelInfoMesh
import good.damn.engine.models.json.MGMLevelInfoTextures
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.runnables.MGRunnableCompileShaders
import good.damn.engine.shader.MGShaderSource
import good.damn.engine.shader.generators.MGGeneratorShader
import good.damn.engine.utils.MGUtilsAsset
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

                val fileName = mesh.a3dMesh
                val diffuse = mesh.textures.textures[
                    0
                ].diffuseMapName

                val builderMaterial = MGMaterialTexture.Builder()

                val generatorShader = MGGeneratorShader(
                    informator.shaders.source
                )

                buildTextureIfExists(
                    "${diffuse}.jpg"
                ) { builderMaterial.textureDiffuse(it) }

                "${diffuse}_m.jpg".let {
                    if (textureExists(it)) {
                        builderMaterial.textureMetallic(it)
                        generatorShader.apply {
                            specular()
                            metallicMap()
                        }
                        return@let
                    }

                    generatorShader.apply {
                        specularNo()
                        metallicNo()
                    }
                }

                "${diffuse}_e.jpg".let {
                    if (textureExists(it)) {
                        builderMaterial.textureEmissive(it)
                        generatorShader.emissiveMap()
                        return@let
                    }
                    generatorShader.emissiveNo()
                }


                "${diffuse}_o.jpg".let {
                    if (textureExists(it)) {
                        builderMaterial.textureOpacity(it)
                        generatorShader.opacityMap()
                        return@let
                    }
                    generatorShader.opacityNo()
                }


                "${diffuse}_n.jpg".let {
                    if (textureExists(it)) {
                        builderMaterial.textureNormal(it)
                        generatorShader.normalMapping()
                        return@let
                    }
                    generatorShader.normalVertex()
                }

                generatorShader.lighting()

                val fragmentCode = generatorShader.generate()

                var cachedShader = informator.shaders.opaqueGeneratedInstanced[
                    fragmentCode
                ]

                if (cachedShader == null) {
                    cachedShader = MGShaderOpaque()
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

                lMeshes[name] = MGProp(
                    fileName,
                    builderMaterial.build(),
                    cachedShader,
                    !(mNonCullFaceMeshes?.contains(
                        fileName
                    ) ?: false),
                    LinkedList(),
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

    private inline fun buildTextureIfExists(
        textureName: String,
        call: ((String)->Unit)
    ) {
        if (textureExists(textureName)) {
            call(textureName)
            return
        }
    }

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