package good.damn.wrapper.hud.ui.clicks

import good.damn.wrapper.imports.APIImport
import good.damn.wrapper.interfaces.APIListenerOnGetUserContent
import good.damn.wrapper.interfaces.APIRequestUserContent
import good.damn.wrapper.models.APMUserContent
import good.damn.hud.UIIClick
import java.util.LinkedList

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
        userContent: APMUserContent
    ) {
        for (it in imports) {
            if (it.isValidExtension(
                userContent.fileName
            )) {
                it.processUserContent(
                    userContent
                )
                return
            }
        }
    }
}