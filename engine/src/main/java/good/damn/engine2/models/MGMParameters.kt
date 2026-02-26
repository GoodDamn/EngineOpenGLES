package good.damn.engine2.models

import good.damn.apigl.drawers.GLDrawerMeshMaterialNormals
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.engine2.logic.MGMGeometryFrustrumMesh

data class MGMParameters(
    var canDrawTriggers: Boolean,
    var currentEditMesh: MGMMeshDrawer<
        GLShaderGeometryPassModel,
        MGMGeometryFrustrumMesh<
            GLDrawerMeshMaterialNormals
        >
    >?
)