package good.damn.common.camera

interface COICameraProjection
: COICameraPosition {
    val projection: FloatArray

    fun setPerspective(
        width: Int,
        height: Int
    )
}