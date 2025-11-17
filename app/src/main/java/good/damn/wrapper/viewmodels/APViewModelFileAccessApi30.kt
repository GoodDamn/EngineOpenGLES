package good.damn.wrapper.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

@RequiresApi(
    value = Build.VERSION_CODES.R
) class APViewModelFileAccessApi30(
    private val callback: ActivityResultCallback<ActivityResult>
): APIViewModelFileAccess {

    private var mLauncher: ActivityResultLauncher<Intent>? = null

    override fun registerLauncher(
        activity: AppCompatActivity
    ) {
        mLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            callback
        )
    }

    override fun unregisterLauncher() {
        mLauncher?.unregister()
    }

    override fun requestPermissionAllFiles(
        packageName: String
    ) {
        mLauncher?.run {
            val intent = Intent().apply {
                Log.d("TAG", "requestPermissionAllFiles: $packageName")
                setData(
                    Uri.parse("package:$packageName")
                )
            }

            try {
                intent.setAction(
                    Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                )
                launch(
                    intent
                )
            } catch (e: Exception) {
                intent.setAction(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                )

                launch(
                    intent
                )
            }
        }
    }

    override fun isExternalStorageManager(
        context: Context
    ) = Environment.isExternalStorageManager()
}