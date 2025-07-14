package good.damn.engine.touch

interface MGIListenerTransform {

    fun onRotate(
        dx: Float,
        dy: Float
    )

    fun onScale(
        scale: Float
    )
}