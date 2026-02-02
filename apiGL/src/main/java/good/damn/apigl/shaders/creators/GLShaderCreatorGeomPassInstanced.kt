package good.damn.apigl.shaders.creators

import good.damn.apigl.shaders.GLShaderGeometryPassInstanced
import good.damn.apigl.shaders.GLShaderMaterial

class GLShaderCreatorGeomPassInstanced
: GLIShaderCreator<
    GLShaderGeometryPassInstanced,
    Array<GLShaderMaterial>
> {
    override fun create(
        param: Array<GLShaderMaterial>
    ) = GLShaderGeometryPassInstanced(
        param
    )
}