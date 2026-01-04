package good.damn.engine.opengl.models

data class MGMMeshDrawer<
    SHADER,
    DRAWER
>(
    var shader: SHADER,
    val drawer: DRAWER
)