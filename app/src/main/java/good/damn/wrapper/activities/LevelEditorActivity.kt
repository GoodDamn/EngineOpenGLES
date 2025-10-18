package good.damn.wrapper.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private var mContentLauncher: ContentLauncher? = null

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

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
           if (Environment.isExternalStorageManager()) {
               initContentView()
               return
           }

           val intent = Intent()
           try {
               intent.setAction(
                   Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
               )
               startActivity(intent)
           } catch (e: Exception) {
               intent.setAction(
                   Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
               )
               startActivity(intent)
           }
           return
       }

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        var isNotGrantedAll = false
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(
               context,
               it
            ) != PackageManager.PERMISSION_GRANTED) {
                isNotGrantedAll = true
            }
        }

        if (!isNotGrantedAll) {
            initContentView()
            return
        }

        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {}

        launcher.launch(
            permissions
        )
    }

    override fun onDestroy() {
        mContentLauncher?.unregister()
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

    private fun initContentView() {
        setContentView(
            LevelEditorView(
                this,
                this
            )
        )
    }
}