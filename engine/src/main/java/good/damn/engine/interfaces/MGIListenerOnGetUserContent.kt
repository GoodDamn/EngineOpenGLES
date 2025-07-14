package good.damn.engine.interfaces

import good.damn.engine.opengl.models.UserContent

interface MGIListenerOnGetUserContent {
    fun onGetUserContent(
        userContent: UserContent
    )
}