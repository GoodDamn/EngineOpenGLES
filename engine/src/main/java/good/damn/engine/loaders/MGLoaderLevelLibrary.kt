package good.damn.engine.loaders

import good.damn.engine.models.MGProp
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.utils.MGUtilsFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.LinkedList

class MGLoaderLevelLibrary(
    private val scope: CoroutineScope,
    localPath: String
) {

    private val mFile = MGUtilsFile.getPublicFile(
        localPath
    )

    private var mJson: JSONArray? = null

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
            mJson = JSONObject(
                String(
                    mFile.inputStream().run {
                        val b = readBytes()
                        close()
                        return@run b
                    },
                    Charset.forName("UTF-8")
                )
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

                lMeshes[name] = MGProp(
                    mesh.getString(
                        "file"
                    ),
                    "${diffuse}.png",
                    "${diffuse}_m.jpg",
                    "${diffuse}_e.jpg",
                    LinkedList()
                )
            }

            meshes = lMeshes
        }
    }

}