package good.damn.wrapper.interfaces

import good.damn.engine.opengl.models.MGMUserContent

interface MGIListenerOnGetUserContent {
    fun onGetUserContent(
        userContent: MGMUserContent
    )
}