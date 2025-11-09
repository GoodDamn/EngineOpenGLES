package good.damn.wrapper.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import good.damn.wrapper.callbacks.APCallbackResultAllFiles

@RequiresApi(
    value = Build.VERSION_CODES.R
) class APViewModelFileAccessApi30
: APIViewModelFileAccess {

    private var mLauncher: ActivityResultLauncher<Intent>? = null

    override fun registerLauncher(
        activity: AppCompatActivity
    ) {
        mLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            APCallbackResultAllFiles()
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
                setData(
                    Uri.parse("package: $packageName")
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