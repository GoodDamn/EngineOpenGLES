package good.damn.engine.opengl.shaders.creators

import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGShaderCreatorGeomPassModel
: MGIShaderCreator<
    MGShaderGeometryPassModel,
    Array<MGShaderMaterial>
> {
    override fun create(
        param: Array<MGShaderMaterial>
    ) = MGShaderGeometryPassModel(
        param
    )
}