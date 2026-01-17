package good.damn.wrapper.interfaces

import good.damn.wrapper.models.MGMUserContent

interface MGIListenerOnGetUserContent {
    fun onGetUserContent(
        userContent: MGMUserContent
    )
}