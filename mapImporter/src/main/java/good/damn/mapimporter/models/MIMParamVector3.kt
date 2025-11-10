package good.damn.mapimporter.models

import good.damn.mapimporter.misc.MIMVector3
import good.damn.mapimporter.utils.MIUtilsIO
import java.io.DataInputStream

data class MIMParamVector3(
    val name: String,
    val value: MIMVector3
)