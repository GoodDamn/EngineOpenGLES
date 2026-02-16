package good.damn.engine2.logic

data class MGMGeometryFrustrumMesh<DRAWER>(
    override var isOn: Boolean,
    val drawer: DRAWER
): MGIGeometryFrustrum