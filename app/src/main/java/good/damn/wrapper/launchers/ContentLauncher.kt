package good.damn.wrapper.launchers

import android.net.Uri
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import good.damn.wrapper.contracts.APContractActivityGetContent

class ContentLauncher(
    activity: AppCompatActivity,
    callback: ActivityResultCallback<Uri?>
): ActivityResultLauncher<Array<String>>(){

    private val mContentBrowser = activity.registerForActivityResult(
        APContractActivityGetContent(),
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

    override fun getContract() = APContractActivityGetContent()

}