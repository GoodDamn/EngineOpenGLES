package good.damn.engine.ui.clicks

import good.damn.engine.imports.MGIImport
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.ui.MGIClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MGClickImport(
    private val imports: Array<MGIImport>,
    private val requester: MGIRequestUserContent
): MGIClick,
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