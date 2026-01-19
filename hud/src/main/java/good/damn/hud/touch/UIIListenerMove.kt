package good.damn.hud.touch

interface UIIListenerMove {
    fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    )
}