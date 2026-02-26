package good.damn.engine2.models

data class MGMMeshDrawer<
    SHADER,
    DRAWER
>(
    var shader: SHADER,
    val drawer: DRAWER
)