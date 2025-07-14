package good.damn.wrapper.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.models.UserContent
import good.damn.wrapper.launchers.ContentLauncher
import good.damn.wrapper.views.LevelEditorView

class LevelEditorActivity
: AppCompatActivity(),
ActivityResultCallback<Uri?>, MGIRequestUserContent {

    companion object {
        private const val TAG = "LevelEditorActivity"
    }

    private lateinit var mContentLauncher: ContentLauncher

    private var mCallbackRequestUserContent: MGIListenerOnGetUserContent? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        val context = this

        mContentLauncher = ContentLauncher(
            context,
            context
        )

        setContentView(
            LevelEditorView(
                this,
                this
            )
        )
    }

    override fun onActivityResult(
        result: Uri?
    ) {
        if (result == null) {
            return
        }

        val extension = result.toString()

        val mimeType = contentResolver.getType(
            result
        ) ?: return

        val stream = contentResolver.openInputStream(
            result
        ) ?: return

        mCallbackRequestUserContent?.onGetUserContent(
            UserContent(
                extension,
                stream
            )
        )
        mCallbackRequestUserContent = null
    }

    override fun requestUserContent(
        callback: MGIListenerOnGetUserContent,
        mimeType: String
    ) {
        mCallbackRequestUserContent = callback
        mContentLauncher.launch(
            mimeType
        )
    }
}