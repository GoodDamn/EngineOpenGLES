package good.damn.engine.touch

interface MGIListenerMove {
    fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    )
}