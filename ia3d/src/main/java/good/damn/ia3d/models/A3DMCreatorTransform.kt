package good.damn.ia3d.models

data class A3DMCreatorTransform(
    val transforms: Array<A3DMTransform>,
    val parentIds: IntArray
)