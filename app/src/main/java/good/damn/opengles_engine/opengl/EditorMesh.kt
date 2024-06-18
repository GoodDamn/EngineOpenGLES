package good.damn.opengles_engine.opengl

import android.graphics.Mesh

data class EditorMesh(
    var objName: String,
    var texName: String,
    var position: Vector,
    var rotation: Vector,
    var scale: Vector,
    var specIntensity: Byte,
    var shininess: Byte,
    var mesh: StaticMesh? = null
)