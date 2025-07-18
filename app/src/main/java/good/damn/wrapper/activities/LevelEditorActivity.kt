package good.damn.wrapper.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.models.MGMUserContent
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

        val windowController = WindowCompat.getInsetsController(
            window,
            window.decorView
        )

        windowController.systemBarsBehavior = WindowInsetsControllerCompat
            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            window.decorView
        ) { view, windowInsets ->

            windowController.hide(
                WindowInsetsCompat.Type.systemBars()
            )

            return@setOnApplyWindowInsetsListener ViewCompat
                .onApplyWindowInsets(
                    view,
                    windowInsets
                )
        }

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
            MGMUserContent(
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