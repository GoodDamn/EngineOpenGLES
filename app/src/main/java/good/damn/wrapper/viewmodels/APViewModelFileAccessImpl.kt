package good.damn.wrapper.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class APViewModelFileAccessImpl(
    private val callback: ActivityResultCallback<Map<String,Boolean>>
): APIViewModelFileAccess {

    private val mPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var mLauncher: ActivityResultLauncher<
        Array<String>
    >? = null

    override fun registerLauncher(
        activity: AppCompatActivity
    ) {
        mLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            callback
        )
    }

    override fun unregisterLauncher() {
        mLauncher?.unregister()
    }

    override fun requestPermissionAllFiles(
        packageName: String
    ) {
        mLauncher?.launch(
            mPermissions
        )
    }

    override fun isExternalStorageManager(
        context: Context
    ): Boolean {
        var isNotGrantedAll = false
        mPermissions.forEach {
            if (ContextCompat.checkSelfPermission(
                context,
                it
            ) != PackageManager.PERMISSION_GRANTED) {
                isNotGrantedAll = true
            }
        }

        return !isNotGrantedAll
    }

}