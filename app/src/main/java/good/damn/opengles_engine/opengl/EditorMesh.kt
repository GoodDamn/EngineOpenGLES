package good.damn.opengles_engine.opengl

import android.graphics.Mesh
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