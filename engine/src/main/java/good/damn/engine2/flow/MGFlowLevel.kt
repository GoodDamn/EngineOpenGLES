package good.damn.engine2.flow

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