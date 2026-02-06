package good.damn.hud

class UIButton(
    private val click: UIIClick
) {

    private var mX = 0f
    private var mY = 0f
    private var mWidth = 0f
    private var mHeight = 0f

    fun bounds(
        x: Float,
        y: Float,
        width: Float,
        height: Float
    ) {
        mX = x
        mY = y
        mWidth = width
        mHeight = height
    }

    fun x(i: Float) = apply {
        mX = i
    }

    fun y(i: Float) = apply {
        mY = i
    }

    fun width(i: Float) = apply {
        mWidth = i
    }

    fun height(i: Float) = apply {
        mHeight = i
    }

    fun intercept(
        x: Float,
        y: Float
    ): Boolean {
        if (mX > x || x > mX + mWidth) {
            return false
        }

        if (mY > y || y > mY + mHeight) {
            return false
        }

        click.onClick()
        return true
    }

}