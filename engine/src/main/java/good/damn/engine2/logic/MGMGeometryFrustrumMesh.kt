package good.damn.engine2.logic

import good.damn.apigl.drawers.GLDrawerMeshMaterialMutable

data class MGMGeometryFrustrumMesh<DRAWER>(
    override var isOn: Boolean,
    val drawer: DRAWER
): MGIGeometryFrustrum