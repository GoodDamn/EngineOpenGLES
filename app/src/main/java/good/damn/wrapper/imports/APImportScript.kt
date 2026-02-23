package good.damn.wrapper.imports

import android.util.Log
import android.util.SparseArray
import androidx.collection.SparseArrayCompat
import good.damn.engine2.files.MGFile
import good.damn.engine2.managers.MGManagerScriptsAssociate
import good.damn.engine2.providers.MGIProviderGLRegister
import good.damn.engine2.providers.MGMProviderGL
import good.damn.script.SCIScript
import good.damn.script.SCManagerScripts
import good.damn.script.SCScriptLightPlacement
import good.damn.wrapper.models.APMUserContent
import java.io.IOException

class APImportScript(
    private val managerScripts: MGManagerScriptsAssociate
): APIImport {

    private companion object {
        private const val EXTENSION = ".jar"
    }

    private var mIndexSubstring = -1

    override fun isValidExtension(
        fileName: String
    ): Boolean {
        mIndexSubstring = fileName.indexOf(
            EXTENSION
        )

        return mIndexSubstring > 0
    }

    override fun processUserContent(
        userContent: APMUserContent,
        contextUserContents: Array<APMUserContent?>,
        offsetContextUserContents: Int
    ) {
        if (mIndexSubstring <= 0) {
            return
        }

        val scriptName = userContent.fileName.take(
            mIndexSubstring
        )

        try {
            managerScripts.load(
                scriptName
            )
        } catch (e: IOException) {
            Log.d("APImportScript", "processUserContent: ${e.message}")
        }

        userContent.stream.close()
    }

}