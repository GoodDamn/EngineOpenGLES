package good.damn.engine.opengl.models

import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel

data class MGMShaderMaterialModel(
    val shader: MGShaderGeometryPassModel,
    val material: Array<MGMaterial>
)