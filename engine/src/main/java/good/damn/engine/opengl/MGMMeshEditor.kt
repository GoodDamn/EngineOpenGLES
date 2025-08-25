package good.damn.engine.opengl

import java.io.File

data class MGMMeshEditor(
    var objFile: File,
    var texName: String,
    var position: MGVector,
    var rotation: MGVector,
    var scale: MGVector,
    var specIntensity: Byte,
    var shininess: Byte,
    //var mesh: MGMeshStatic? = null
)