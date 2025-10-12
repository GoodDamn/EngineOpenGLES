package good.damn.engine.ui.clicks

import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.generators.MGGeneratorLandscape
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.ui.MGIClick

class MGClickImportMesh(
    private val handler: MGHandlerGl,
    private val requester: MGIRequestUserContent
): MGIClick,
MGIListenerOnGetUserContent,
Runnable {


    override fun onClick() {
        requester.requestUserContent(
            this,
            "application/*"
        )
    }

    override fun onGetUserContent(
        userContent: MGMUserContent
    ) {

        handler.post(
            this
        )
    }

    override fun run() {

    }
}