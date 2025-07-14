package good.damn.opengles_engine.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.models.UserContent
import good.damn.opengles_engine.App
import good.damn.opengles_engine.launchers.ContentLauncher
import good.damn.opengles_engine.views.LevelEditorView
import good.damn.opengles_engine.views.touchable.Axis
import good.damn.opengles_engine.views.touchable.AxisView

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

        val viewLevelEditor = LevelEditorView(
            this,
            this
        )

        val axisView = AxisView(
            context
        ).apply {
            axisMoves = arrayOf(
                Axis(
                    0xffff0000.toInt()
                ) {
                  viewLevelEditor.onChangeMeshPosition(
                      it,
                      0f,
                      0f
                  )
                },
                Axis(
                    0xff00ff00.toInt()
                ) {
                    viewLevelEditor.onChangeMeshPosition(
                        0f,
                        it,
                        0f
                    )
                },
                Axis(
                    0xff0000ff.toInt()
                ) {
                    viewLevelEditor.onChangeMeshPosition(
                        0f,
                        0f,
                        it
                    )
                }
            )
        }

        setContentView(
            LinearLayout(
                context
            ).apply {
                orientation = LinearLayout
                    .VERTICAL

                addView(
                    viewLevelEditor,
                    -1,
                    (App.HEIGHT * 0.7f).toInt()
                )

                addView(
                    axisView,
                    -1,
                    (App.HEIGHT * 0.2f).toInt()
                )
            }
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