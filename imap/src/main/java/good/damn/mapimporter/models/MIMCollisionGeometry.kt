package good.damn.mapimporter.models

data class MIMCollisionGeometry(
    val boxes: List<MIMCollisionBox>,
    val planes: List<MIMCollisionPlane>,
    val triangles: List<MIMCollisionTriangle>,
)