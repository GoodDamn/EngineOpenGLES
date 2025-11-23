package good.damn.engine.flow

class MGFlowLevel<T>(
    private val callbackEmit: (T) -> Unit
) {

    fun emit(
        data: T
    ) {
        callbackEmit(
            data
        )
    }
}