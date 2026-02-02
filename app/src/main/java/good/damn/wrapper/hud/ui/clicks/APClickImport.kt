package good.damn.wrapper.hud.ui.clicks

import good.damn.wrapper.imports.MGIImport
import good.damn.wrapper.interfaces.MGIListenerOnGetUserContent
import good.damn.wrapper.interfaces.MGIRequestUserContent
import good.damn.wrapper.models.MGMUserContent
import good.damn.hud.UIIClick

class APClickImport(
    private val imports: Array<MGIImport>,
    private val requester: MGIRequestUserContent
): UIIClick,
    MGIListenerOnGetUserContent {

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
        userContent: MGMUserContent
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