package good.damn.wrapper.callbacks

import androidx.activity.result.ActivityResultCallback
import good.damn.wrapper.activities.LevelEditorActivity
import good.damn.wrapper.viewmodels.APIViewModelFileAccess

class APCallbackResultAllFiles(
    private val activity: LevelEditorActivity
): ActivityResultCallback<Map<String,Boolean>> {

    override fun onActivityResult(
        result: Map<String, Boolean>
    ) {
        var isNotGrantedAll = false

        result.forEach {
            if (it.value) {
                return@forEach
            }

            isNotGrantedAll = true
        }

        if (isNotGrantedAll) {
            // repeat permissions
            activity.requestPermissionAllFiles()
            return
        }
        activity.initContentView()
    }

}