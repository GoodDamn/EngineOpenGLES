package good.damn.engine.ui.clicks

import android.util.Log
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
        if (containsModel(userContent.uri)) {
            return
        }
    }

    private inline fun containsModel(
        uri: String
    ) = uri.contains("fbx") ||
        uri.contains("3ds") ||
        uri.contains("obj")
}