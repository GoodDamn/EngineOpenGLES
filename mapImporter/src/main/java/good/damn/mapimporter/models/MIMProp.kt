package good.damn.mapimporter.models

import good.damn.mapimporter.misc.MIMVector3

data class MIMProp(
    val groupName: String?,
    val id: Int,
    val libName: String,
    val materialId: Int,
    val name: String,
    val position: MIMVector3,
    val rotation: MIMVector3?,
    val scale: MIMVector3?
)