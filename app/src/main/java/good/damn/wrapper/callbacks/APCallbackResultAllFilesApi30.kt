package good.damn.wrapper.callbacks

import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.annotation.RequiresApi
import good.damn.wrapper.activities.LevelEditorActivity
import good.damn.wrapper.viewmodels.APIViewModelFileAccess

@RequiresApi(
    value = Build.VERSION_CODES.R
) class APCallbackResultAllFilesApi30(
    private val activity: LevelEditorActivity
): ActivityResultCallback<
    ActivityResult
> {
    override fun onActivityResult(
        result: ActivityResult
    ) {
        if (Environment.isExternalStorageManager()) {
            activity.initContentView()
            return
        }
        activity.requestPermissionAllFiles()
    }
}