package good.damn.wrapper.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.WindowManager
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import good.damn.wrapper.interfaces.MGIListenerOnGetUserContent
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.wrapper.models.MGMUserContent
import good.damn.wrapper.callbacks.APCallbackResultAllFiles
import good.damn.wrapper.callbacks.APCallbackResultAllFilesApi30
import good.damn.wrapper.launchers.ContentLauncher
import good.damn.wrapper.viewmodels.APIViewModelFileAccess
import good.damn.wrapper.viewmodels.APViewModelFileAccessApi30
import good.damn.wrapper.viewmodels.APViewModelFileAccessImpl
import good.damn.wrapper.views.LevelEditorView

class LevelEditorActivity
: AppCompatActivity(),
ActivityResultCallback<Uri?>, MGIRequestUserContent {

    companion object {
        private const val TAG = "LevelEditorActivity"
    }

    private var mContentLauncher: ContentLauncher? = null
    private var mViewModelAllFiles: APIViewModelFileAccess? = null
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
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
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

        val viewModel = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
        ) APViewModelFileAccessApi30(
            APCallbackResultAllFilesApi30(
                this
            )
        ) else APViewModelFileAccessImpl(
            APCallbackResultAllFiles(
                this
            )
        )

        if (viewModel.isExternalStorageManager(
            context
        )) {
            initContentView()
            return
        }

        mViewModelAllFiles = viewModel

        viewModel.registerLauncher(
            this
        )

        requestPermissionAllFiles()
    }

    override fun onDestroy() {
        mContentLauncher?.unregister()
        mViewModelAllFiles?.unregisterLauncher()
        super.onDestroy()
    }

    override fun onActivityResult(
        result: Uri?
    ) {
        if (result == null) {
            return
        }

        val mimeType = contentResolver.getType(
            result
        ) ?: return

        val fileName = contentResolver.query(
            result, null, null, null, null
        )?.run {
            val nameIndex = getColumnIndex(
                OpenableColumns.DISPLAY_NAME
            )
            moveToFirst()
            val name = getString(
                nameIndex
            )
            close()
            return@run name
        } ?: return

        val stream = contentResolver.openInputStream(
            result
        ) ?: return

        mCallbackRequestUserContent?.onGetUserContent(
            MGMUserContent(
                fileName,
                mimeType,
                stream
            )
        )
        mCallbackRequestUserContent = null
    }

    override fun requestUserContent(
        callback: MGIListenerOnGetUserContent,
        mimeType: Array<String>
    ) {
        mCallbackRequestUserContent = callback
        mContentLauncher?.launch(
            mimeType
        )
    }

    fun requestPermissionAllFiles() {
        mViewModelAllFiles?.requestPermissionAllFiles(
            application.packageName
        )
    }

    fun initContentView() {
        setContentView(
            LevelEditorView(
                this,
                this
            )
        )
    }
}