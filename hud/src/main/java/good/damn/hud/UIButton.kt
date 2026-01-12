package good.damn.hud

class UIButton(
    private var mX: Float = 0f,
    private var mY: Float = 0f,
    private var mWidth: Float = 0f,
    private var mHeight: Float = 0f,
    private val click: UIIClick
) {
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