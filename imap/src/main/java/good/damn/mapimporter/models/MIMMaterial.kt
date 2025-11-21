package good.damn.mapimporter.models

data class MIMMaterial(
    val id: Int,
    val name: String,
    val scalarParam: List<MIMParamScalar>?,
    val shader: String,
    val textureParams: List<MIMParamTexture>,
    val vector2Params: List<MIMParamVector2>?,
    val vector3Params: List<MIMParamVector3>?,
    val vector4Params: List<MIMParamVector4>?
)