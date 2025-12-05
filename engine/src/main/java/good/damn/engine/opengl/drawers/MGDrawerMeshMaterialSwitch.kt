package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGDrawerMeshMaterialSwitch(
    private val material: Array<MGMaterial>,
    drawMesh: MGDrawerMeshSwitch
): MGDrawerMeshTextureSwitch(
    material,
    drawMesh
) {
    fun drawMaterial(
        shaderMaterial: Array<MGShaderMaterial>,
        shaderModel: MGIShaderModel
    ) {
        for (i in material.indices) {
            material[i].draw(
                shaderMaterial[i]
            )
        }

        mDrawerMesh.draw(
            shaderModel
        )

        for (i in material.indices) {
            material[i].unbind(
                shaderMaterial[i]
            )
        }
    }
}