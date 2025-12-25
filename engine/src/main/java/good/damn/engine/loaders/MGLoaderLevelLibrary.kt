package good.damn.engine.loaders

import android.util.Log
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGProp
import good.damn.engine.models.json.MGMLevelInfoMesh
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import good.damn.engine.opengl.shaders.MGShaderTexture
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.shader.generators.MGGeneratorMaterialG
import good.damn.engine.shader.generators.MGMaterialShader
import good.damn.engine.utils.MGUtilsFile
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset
import java.util.LinkedList

class MGLoaderLevelLibrary(
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
        val src = informator.shaders.source

        val shaderMaterials = LinkedList<MGShaderMaterial>()
        val buildersMaterial = LinkedList<MGMaterialTexture>()
        val srcCodeBuilder = StringBuilder()

        for (index in 0 until 1) {
            val diffuse = textures[index].diffuseMapName
            val controlMapName = textures[index].controlMapName

            val materialShader = MGMaterialShader.Builder(
                diffuse,
                localPathLibTextures,
                src
            ).diffuse()
                .opacity()
                .specular()
                .emissive(0.0f)
                .useDepthFunc()
                .normal()
                .build()

            srcCodeBuilder.append(
                materialShader.srcCodeMaterial
            )

            val texCoordScale = if (
                controlMapName == null
            ) 1f else 105f

            shaderMaterials.add(
                MGShaderMaterial(
                    materialShader.shaderTextures.toTypedArray()
                )
            )

            buildersMaterial.add(
                materialShader.materialTexture
            )

            if (controlMapName == null) {
                break
            }
        }

        val fragmentCode = srcCodeBuilder.toString()

        var cachedShader = informator.shaders.opaqueGeneratedInstanced[
            fragmentCode
        ]

        if (cachedShader == null) {
            cachedShader = MGShaderGeometryPassInstanced(
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
        }

        return MGProp(
            fileName,
            buildersMaterial.toTypedArray(),
            cachedShader,
            !(mNonCullFaceMeshes?.contains(
                fileName
            ) ?: false),
            LinkedList(),
        )
    }


    fun readProps() {
        val json = mJson
            ?: return

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