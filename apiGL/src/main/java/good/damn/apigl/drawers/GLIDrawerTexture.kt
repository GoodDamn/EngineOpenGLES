package good.damn.apigl.drawers

interface GLIDrawerTexture<T>
: GLIDrawerShader<T> {
    fun unbind(
        shader: T
    )
}