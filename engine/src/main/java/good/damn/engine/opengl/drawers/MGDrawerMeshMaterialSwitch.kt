package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGDrawerMeshMaterialSwitch(
    private val material: MGMaterial,
    drawMesh: MGDrawerMeshSwitch
): MGDrawerMeshTextureSwitch(
    material,
    drawMesh
) {
    fun drawMaterial(
        shaderMaterial: MGShaderMaterial,
        shaderModel: MGIShaderModel
    ) {
        material.draw(
            shaderMaterial
        )

        mDrawerMesh.draw(
            shaderModel
        )

        material.unbind(
            shaderMaterial
        )
    }
}