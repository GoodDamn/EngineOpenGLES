package good.damn.engine.interfaces

import good.damn.engine.opengl.models.MGMUserContent

interface MGIListenerOnGetUserContent {
    fun onGetUserContent(
        userContent: MGMUserContent
    )
}