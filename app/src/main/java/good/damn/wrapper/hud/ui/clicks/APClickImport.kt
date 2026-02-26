package good.damn.wrapper.hud.ui.clicks

import good.damn.wrapper.imports.APIImport
import good.damn.wrapper.interfaces.APIListenerOnGetUserContent
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.wrapper.models.APMUserContent
import good.damn.hud.UIIClick

class APClickImport(
    private val requester: APIRequestUserContent,
    private val imports: Array<APIImport>
): UIIClick,
APIListenerOnGetUserContent {

    override fun onClick() {
        requester.requestUserContent(
            this,
            arrayOf(
                "*/*",
                //"application/3ds",
                //"application/obj"
            )
        )
    }

    override fun onGetUserContent(
        userContents: Array<APMUserContent?>
    ) {
        // It works in case if we have
        // only one root uri
        if (userContents.isEmpty()) {
            return
        }

        val rootContent = userContents[0]
            ?: return

        imports.find {
            it.isValidExtension(
                rootContent.fileName
            )
        }?.processUserContent(
            rootContent,
            userContents,
            1
        )
    }
}