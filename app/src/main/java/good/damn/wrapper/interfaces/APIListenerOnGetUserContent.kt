package good.damn.wrapper.interfaces

import good.damn.wrapper.models.APMUserContent

interface APIListenerOnGetUserContent {
    fun onGetUserContent(
        userContent: APMUserContent
    )
}