package good.damn.opengles_engine.opengl

data class EditorMesh(
    val objName: String,
    val texName: String,
    val position: Vector,
    val rotation: Vector,
    val scale: Vector,
    val specIntensity: Byte,
    val shininess: Byte
)