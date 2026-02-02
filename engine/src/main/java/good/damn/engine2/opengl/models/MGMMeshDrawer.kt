package good.damn.engine2.opengl.models

data class MGMMeshDrawer<
    SHADER,
    DRAWER
>(
    var shader: SHADER,
    val drawer: DRAWER
)