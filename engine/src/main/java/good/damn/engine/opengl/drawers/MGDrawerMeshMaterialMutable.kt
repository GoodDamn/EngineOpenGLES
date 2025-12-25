package good.damn.engine.opengl.drawers

import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.shaders.MGIShaderModel
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial

class MGDrawerMeshMaterialMutable(
    var shader: MGShaderGeometryPassModel,
    var material: Array<MGMaterial>,
    var drawerMesh: MGDrawerMeshSwitch
) {
    fun drawNormals(
        shader: MGIShaderNormal
    ) {
        drawerMesh.drawNormals(
            shader
        )
    }

    fun drawVertices(
        shader: MGIShaderModel
    ) {
        drawerMesh.draw(
            shader
        )
    }

    fun drawMaterials(
        shaderMaterial: Array<MGShaderMaterial>,
        shaderModel: MGIShaderModel
    ) {
        for (i in material.indices) {
            material[i].draw(
                shaderMaterial[i]
            )
        }

        drawerMesh.draw(
            shaderModel
        )

        for (i in material.indices) {
            material[i].unbind(
                shaderMaterial[i]
            )
        }
    }
}