package good.damn.engine.opengl.models

import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel

data class MGMPoolMaterial(
    val shader: MGShaderGeometryPassModel,
    val material: MGMaterial
)