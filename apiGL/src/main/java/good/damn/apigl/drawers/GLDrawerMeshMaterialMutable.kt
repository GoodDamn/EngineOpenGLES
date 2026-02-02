package good.damn.apigl.drawers

import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLIShaderNormal
import good.damn.apigl.shaders.GLShaderMaterial

class GLDrawerMeshMaterialMutable(
    var material: Array<GLMaterial>,
    private val drawerMesh: GLDrawerMesh
) {
    fun drawNormals(
        shader: GLIShaderNormal
    ) {
        drawerMesh.drawNormals(
            shader
        )
    }

    fun drawMaterials(
        shaderMaterial: Array<GLShaderMaterial>,
        shaderModel: GLIShaderModel
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