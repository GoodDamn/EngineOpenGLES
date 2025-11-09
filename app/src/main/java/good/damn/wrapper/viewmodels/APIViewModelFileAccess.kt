package good.damn.wrapper.viewmodels

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ComponentActivity

interface APIViewModelFileAccess {

    fun registerLauncher(
        activity: AppCompatActivity
    )

    fun unregisterLauncher()

    fun requestPermissionAllFiles(
        packageName: String
    )

    fun isExternalStorageManager(
        context: Context
    ): Boolean
}