package good.damn.engine.opengl.models

import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel

data class MGMMeshMaterial(
    var shader: MGShaderGeometryPassModel,
    val drawer: MGDrawerMeshMaterialMutable
)