package good.damn.engine.opengl

import java.io.File

data class EditorMesh(
    var objFile: File,
    var texName: String,
    var position: Vector,
    var rotation: Vector,
    var scale: Vector,
    var specIntensity: Byte,
    var shininess: Byte,
    var mesh: StaticMesh? = null
)