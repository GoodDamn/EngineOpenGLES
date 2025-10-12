package good.damn.wrapper.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class APContractActivityGetContent
: ActivityResultContract<
    Array<String>,
    Uri?
>() {

    override fun createIntent(
        context: Context,
        input: Array<String>
    ) = Intent(
        Intent.ACTION_GET_CONTENT
    ).addCategory(
        Intent.CATEGORY_OPENABLE
    ).setType(
        input[0]
    )

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ) = intent.takeIf {
        resultCode == Activity.RESULT_OK
    }?.data

}