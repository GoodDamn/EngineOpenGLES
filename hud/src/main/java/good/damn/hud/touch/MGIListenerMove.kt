package good.damn.hud.touch

interface MGIListenerMove {
    fun onMove(
        x: Float,
        y: Float,
        directionX: Float,
        directionY: Float
    )
}