package good.damn.mapimporter.models

import good.damn.mapimporter.misc.MIMVector3

data class MIMBatch(
    val materialId: Int,
    val name: String,
    val position: MIMVector3,
    val propIds: String
)