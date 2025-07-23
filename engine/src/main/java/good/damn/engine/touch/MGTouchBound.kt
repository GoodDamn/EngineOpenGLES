package good.damn.engine.touch

open class MGTouchBound
: MGTouchMulti() {

    protected var mHalfBound = 0f
        private set

    protected var mHalfX = 0f
        private set

    protected var mHalfY = 0f
        private set

    protected var mLeft = 0f
        private set

    protected var mTop = 0f
        private set

    protected var mRight = 0f
        private set

    protected var mBottom = 0f
        private set

    fun setBounds(
        left: Float,
        top: Float,
        s: Float
    ) {
        mLeft = left
        mTop = top
        mRight = left + s
        mBottom = top + s
        mHalfBound = s * 0.5f
        mHalfX = (mRight + mLeft) * 0.5f
        mHalfY = (mBottom + mTop) * 0.5f
    }
}