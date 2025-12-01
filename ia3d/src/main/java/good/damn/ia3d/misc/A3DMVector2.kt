package good.damn.ia3d.misc

data class A3DMVector2(
    var x: Float = 0f,
    var y: Float = 0f
) {
    fun subtract(
        v: A3DMVector2,
        vv: A3DMVector2
    ) {
        x = v.x - vv.x
        y = v.y - vv.y
    }
}