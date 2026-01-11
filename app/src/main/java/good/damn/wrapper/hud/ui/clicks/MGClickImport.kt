package good.damn.wrapper.hud.ui.clicks

import good.damn.engine.imports.MGIImport
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.hud.MGIClick

class MGClickImport(
    private val imports: Array<MGIImport>,
    private val requester: MGIRequestUserContent
): good.damn.hud.MGIClick,
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