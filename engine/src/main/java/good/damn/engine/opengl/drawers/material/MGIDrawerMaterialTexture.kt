package good.damn.engine.opengl.drawers.material

import good.damn.engine.opengl.drawers.MGIDrawerTexture
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.textures.MGTexture

interface MGIDrawerMaterialTexture
: MGIDrawerTexture<MGShaderMaterial> {
    var texture: MGTexture
}