package good.damn.engine2.loaders

import good.damn.apigl.drawers.GLDrawerMaterialTexture
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.common.utils.COUtilsFile
import good.damn.engine2.models.MGProp
import good.damn.engine2.models.json.MGMLevelInfoMesh
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.opengl.pools.MGPoolMaterials
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.utils.MGUtilsJson
import org.json.JSONArray
import java.util.LinkedList

class MGLoaderLevelLibrary(
    private val localPathLibTextures: String,
    localPath: String,
    localPathCullFaceList: String,
    private val glProvider: MGMProviderGL
) {

    private val mFile = COUtilsFile.getPublicFile(
        localPath
    )

    private val mFileCullFace = COUtilsFile.getPublicFile(
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

    private val mBinderAttribute = GLBinderAttribute.Builder()
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

        val diffuse = textures[0].diffuseMapName
        val controlMapName = textures[0].controlMapName

        val materialShader = glProvider.pools.materials.loadOrGetFromCache(
            diffuse,
            localPathLibTextures
        )

        val shaderMaterials = LinkedList<GLShaderMaterial>()
        val buildersMaterial = LinkedList<GLMaterial>()

        shaderMaterials.add(
            GLShaderMaterial(
                materialShader.shaderTextures
            )
        )

        buildersMaterial.add(
            GLMaterial(
                GLDrawerMaterialTexture(
                    materialShader.textures
                )
            )
        )
        val shaders = glProvider.shaders

        val shader = shaders.cacheGeometryPassInstanced.loadOrGetFromCache(
            materialShader.srcCodeMaterial,
            shaders.source.verti,
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