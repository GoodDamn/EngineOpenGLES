package good.damn.opengles_engine.activities

import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import good.damn.opengles_engine.App
import good.damn.opengles_engine.launchers.ContentLauncher
import good.damn.opengles_engine.opengl.models.UserContent
import good.damn.opengles_engine.views.LevelEditorView

class LevelEditorActivity
: AppCompatActivity(),
ActivityResultCallback<Uri?> {

    companion object {
        private const val TAG = "LevelEditorActivity"
    }
    
    private lateinit var mLevelEditorView: LevelEditorView
    private lateinit var mContentLauncher: ContentLauncher

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

        val layout = LinearLayout(
            context
        )

        mLevelEditorView = LevelEditorView(
            this
        )

        layout.orientation = LinearLayout
            .VERTICAL


        layout.addView(
            mLevelEditorView,
            -1,
            (App.HEIGHT * 0.7f).toInt()
        )

        setContentView(
            layout
        )
    }

    override fun onActivityResult(
        result: Uri?
    ) {
        if (result == null) {
            return
        }

        val p = result.toString()
        val extension = result.toString()
            .substring(p.length - 4)

        val mimeType = contentResolver.getType(
            result
        ) ?: return

        val stream = contentResolver.openInputStream(
            result
        ) ?: return

        mLevelEditorView.onGetUserContentUri(
            UserContent(
                extension,
                stream
            )
        )
    }

    // External call
    fun loadFromUserDisk(
        mime: String
    ) {
        mContentLauncher.launch(
            mime
        )
    }
}