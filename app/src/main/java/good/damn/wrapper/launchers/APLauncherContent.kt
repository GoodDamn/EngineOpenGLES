package good.damn.wrapper.launchers

import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import good.damn.wrapper.contracts.APContractActivityGetMultipleContent

class APLauncherContent(
    activity: AppCompatActivity,
    callback: ActivityResultCallback<Array<Uri>?>
): ActivityResultLauncher<Array<String>>(){

    private val mContentBrowser = activity.registerForActivityResult(
        APContractActivityGetMultipleContent(),
        callback
    )

    override fun launch(
        input: Array<String>?,
        options: ActivityOptionsCompat?
    ) {
        mContentBrowser.launch(input)
    }

    override fun unregister() {
        mContentBrowser.unregister()
    }

    override fun getContract() = APContractActivityGetMultipleContent()

}