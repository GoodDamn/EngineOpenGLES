package good.damn.wrapper.interfaces

interface APIRequestUserContent {
    fun requestUserContent(
        callback: APIListenerOnGetUserContent,
        mimeType: Array<String>
    )
}