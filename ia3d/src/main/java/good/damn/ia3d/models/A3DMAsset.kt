package good.damn.ia3d.models

data class A3DMAsset(
    val materials: Array<A3DMMaterial>,
    val meshes: Array<A3DMMesh>,
    val transform: A3DMCreatorTransform,
    val objects: Array<A3DMObject>
)
