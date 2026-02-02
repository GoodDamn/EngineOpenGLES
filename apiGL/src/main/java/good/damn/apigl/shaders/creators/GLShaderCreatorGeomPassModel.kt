package good.damn.apigl.shaders.creators

import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.apigl.shaders.GLShaderMaterial

class GLShaderCreatorGeomPassModel
: GLIShaderCreator<
    GLShaderGeometryPassModel,
    Array<GLShaderMaterial>
> {
    override fun create(
        param: Array<GLShaderMaterial>
    ) = GLShaderGeometryPassModel(
        param
    )
}