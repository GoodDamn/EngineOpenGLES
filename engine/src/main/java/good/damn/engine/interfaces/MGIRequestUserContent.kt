package good.damn.engine.interfaces

interface MGIRequestUserContent {
    fun requestUserContent(
        callback: MGIListenerOnGetUserContent,
        mimeType: Array<String>
    )
}