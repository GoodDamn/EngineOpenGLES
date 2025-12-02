package good.damn.engine.loaders

import good.damn.engine.models.MGProp
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.pools.MGPoolTextures
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
    private val poolTextures: MGPoolTextures,
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

    fun loadLibrary(): Boolean {
        isLoadLibrary = false
        if (!mFile.exists()) {
            return false
        }

        scope.launch {
            mJson = fileToJson(
                mFile
            ).getJSONArray(
                "groups"
            ).getJSONObject(
                0
            ).getJSONArray(
                "props"
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

                val mesh = lJson.getJSONObject(
                    "mesh"
                )

                val diffuse = mesh.getJSONArray(
                    "textures"
                ).getJSONObject(0).getString(
                    "diffuseMap"
                )

                val fileName = mesh.getString(
                    "file"
                )

                val materialTexture = MGMaterialTexture.Builder(
                    poolTextures
                ).textureDiffuse(
                    "${diffuse}.jpg"
                ).textureMetallic(
                    "${diffuse}_m.jpg"
                ).textureEmissive(
                    "${diffuse}_e.jpg"
                ).textureOpacity(
                    "${diffuse}_o.jpg"
                ).textureNormal(
                    "${diffuse}_n.jpg"
                ).build()


                lMeshes[name] = MGProp(
                    fileName,
                    materialTexture,
                    !(mNonCullFaceMeshes?.contains(
                        fileName
                    ) ?: false),
                    LinkedList(),
                )
            }

            meshes = lMeshes
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