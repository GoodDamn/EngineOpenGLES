package good.damn.engine.loaders

import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGProp
import good.damn.engine.models.json.MGMLevelInfoMesh
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsJson
import org.json.JSONArray
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

    private val mBinderAttribute = MGBinderAttribute.Builder()
        .bindPosition()
        .bindTextureCoordinates()
        .bindNormal()
        .bindInstancedModel()
        .bindInstancedRotationMatrix()
        .bindTangent()
        .build()

    fun loadLibrary(): Boolean {
        isLoadLibrary = false
        if (!mFile.exists()) {
            return false
        }

        val rootJson = MGUtilsJson.createFromFile(
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

        val jsonList = MGUtilsJson.createFromFile(
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

        val diffuse = textures[0].diffuseMapName
        val controlMapName = textures[0].controlMapName

        val materialShader = informator.poolMaterials.loadOrGetFromCache(
            diffuse,
            localPathLibTextures,
            informator
        )

        val shaderMaterials = LinkedList<MGShaderMaterial>()
        val buildersMaterial = LinkedList<MGMaterial>()

        shaderMaterials.add(
            MGShaderMaterial(
                materialShader.shaderTextures
            )
        )

        buildersMaterial.add(
            MGMaterial(
                materialShader.materialTexture
            )
        )

        val shader = informator.shaders.cacheGeometryPassInstanced.loadOrGetFromCache(
            materialShader.srcCodeMaterial,
            informator.shaders.source.verti,
            mBinderAttribute,
            shaderMaterials.toTypedArray()
        )

        return MGProp(
            fileName,
            buildersMaterial.toTypedArray(),
            shader,
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
}