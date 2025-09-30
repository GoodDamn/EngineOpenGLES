package good.damn.engine.touch

open class MGTouchBound(
    maxTouches: Int = 1
): MGTouchMulti(
    maxTouches
) {

    protected var mLeft = 0f
        private set

    protected var mTop = 0f
        private set

    protected var mRight = 0f
        private set

    protected var mBottom = 0f
        private set

    open fun setBounds(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float
    ) {
        mLeft = left
        mTop = top
        mRight = right
        mBottom = bottom
    }

    open fun setBounds(
        left: Float,
        top: Float,
        s: Float
    ) = setBounds(
        left,
        top,
        left + s,
        top + s
    )
}