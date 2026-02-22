package good.damn.wrapper.imports

import android.util.SparseArray
import androidx.collection.SparseArrayCompat
import good.damn.script.SCIScript
import good.damn.script.SCScriptLightPlacement
import good.damn.wrapper.models.APMUserContent

class APImportScript
: APIImport {

    private companion object {
        private const val EXTENSION = ".jar"
    }

    private val mScriptLoaders = SparseArrayCompat<
        SCIScript
    >().apply {

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
        if (mIndexSubstring > 0) {
            return
        }

        val scriptName = userContent.fileName.take(
            mIndexSubstring
        )



    }

}