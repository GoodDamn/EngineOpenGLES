package good.damn.wrapper.interfaces

interface MGIRequestUserContent {
    fun requestUserContent(
        callback: MGIListenerOnGetUserContent,
        mimeType: Array<String>
    )
}