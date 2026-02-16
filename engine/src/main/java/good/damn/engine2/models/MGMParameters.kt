package good.damn.engine2.models

import good.damn.apigl.drawers.GLDrawerMeshMaterialMutable
import good.damn.apigl.drawers.GLDrawerMeshMaterialNormals
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.engine2.logic.MGMGeometryFrustrumMesh
import good.damn.engine2.opengl.models.MGMMeshDrawer

data class MGMParameters(
    var canDrawTriggers: Boolean,
    var currentEditMesh: MGMMeshDrawer<
        GLShaderGeometryPassModel,
        MGMGeometryFrustrumMesh<
            GLDrawerMeshMaterialNormals
        >
    >?
)