package good.damn.wrapper.callbacks

import androidx.activity.result.ActivityResultCallback
import good.damn.wrapper.activities.APActivityLevelEditor

class APCallbackResultAllFiles(
    private val activity: APActivityLevelEditor
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