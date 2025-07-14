package good.damn.opengles_engine.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import good.damn.opengles_engine.App
import good.damn.opengles_engine.launchers.ContentLauncher
import good.damn.engine.opengl.models.UserContent
import good.damn.opengles_engine.views.LevelEditorView
import good.damn.opengles_engine.views.touchable.Axis
import good.damn.opengles_engine.views.touchable.AxisView

class LevelEditorActivity
: AppCompatActivity(),
ActivityResultCallback<Uri?> {

    companion object {
        private const val TAG = "LevelEditorActivity"
    }

    private lateinit var mLevelEditorView: LevelEditorView
    private lateinit var mContentLauncher: ContentLauncher

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

        mLevelEditorView = LevelEditorView(
            this
        )

        val axisView = AxisView(
            context
        ).apply {
            axisMoves = arrayOf(
                Axis(
                    0xffff0000.toInt()
                ) {
                  mLevelEditorView.onChangeMeshPosition(
                      it,
                      0f,
                      0f
                  )
                },
                Axis(
                    0xff00ff00.toInt()
                ) {
                    mLevelEditorView.onChangeMeshPosition(
                        0f,
                        it,
                        0f
                    )
                },
                Axis(
                    0xff0000ff.toInt()
                ) {
                    mLevelEditorView.onChangeMeshPosition(
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
                    mLevelEditorView,
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
            good.damn.engine.opengl.models.UserContent(
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