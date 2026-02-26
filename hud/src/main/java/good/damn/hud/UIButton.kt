package good.damn.hud

class UIButton(
    private val click: UIIClick
) {

    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f

    fun intercept(
        x: Float,
        y: Float
    ): Boolean {
        if (this.x > x || x > this.x + width) {
            return false
        }

        if (this.y > y || y > this.y + height) {
            return false
        }

        click.onClick()
        return true
    }

}