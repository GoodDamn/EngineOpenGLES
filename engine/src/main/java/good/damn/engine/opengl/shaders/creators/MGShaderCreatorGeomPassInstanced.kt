package good.damn.engine.opengl.shaders.creators

import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGShaderCreatorGeomPassInstanced
: MGIShaderCreator<
    MGShaderGeometryPassInstanced,
    Array<MGShaderMaterial>
> {
    override fun create(
        param: Array<MGShaderMaterial>
    ) = MGShaderGeometryPassInstanced(
        param
    )
}