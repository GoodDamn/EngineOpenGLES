package good.damn.ia3d.enums

enum class A3DEnumTypeBufferVertex(
    val type: Int,
    val vertexSize: Int
) {
    POSITION(1,3),
    UV1(2,2),
    NORMAL1(3,3),
    UV2(4,2),
    COLOR(5,4),
    NORMAL2(6,3)
}