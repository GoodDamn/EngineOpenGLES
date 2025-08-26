package good.damn.engine.ui.clicks

import android.os.Handler
import android.os.Looper
import good.damn.engine.interfaces.MGIListenerOnGetUserContent
import good.damn.engine.interfaces.MGIRequestUserContent
import good.damn.engine.opengl.generators.MGGeneratorLandscape
import good.damn.engine.opengl.maps.MGMapDisplace
import good.damn.engine.opengl.models.MGMUserContent
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.ui.MGIClick

class MGClickGenerateLandscape(
    private val handler: MGHandlerGl,
    private val generatorLandscape: MGGeneratorLandscape,
    private val requester: MGIRequestUserContent
): MGIClick,
MGIListenerOnGetUserContent,
Runnable {

    private var mapDisplace: MGMapDisplace? = null

    override fun onClick() {
        requester.requestUserContent(
            this,
            "*/*"
        )
    }

    override fun onGetUserContent(
        userContent: MGMUserContent
    ) {
        mapDisplace = MGMapDisplace.createFromStream(
            userContent.stream
        )

        handler.post(
            this
        )
    }

    override fun run() {
        mapDisplace?.run {
            generatorLandscape.displace(
                this
            )
            mapDisplace = null
        }
    }
}