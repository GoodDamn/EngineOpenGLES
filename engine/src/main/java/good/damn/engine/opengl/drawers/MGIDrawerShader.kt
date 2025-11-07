package good.damn.engine.opengl.drawers

interface MGIDrawerShader<T> {
    fun draw(
        shader: T
    )
}