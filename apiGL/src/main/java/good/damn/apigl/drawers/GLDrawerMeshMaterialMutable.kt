package good.damn.apigl.drawers

import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLIShaderNormal
import good.damn.apigl.shaders.GLShaderMaterial

data class GLDrawerMeshMaterialMutable(
    var material: Array<GLMaterial>,
    var drawerMesh: GLDrawerMesh
) {
    companion object {
        @JvmStatic
        fun draw(
            drawer: GLDrawerMeshMaterialMutable,
            shaderMaterial: Array<GLShaderMaterial>,
            shaderModel: GLIShaderModel
        ) {
            for (i in drawer.material.indices) {
                drawer.material[i].draw(
                    shaderMaterial[i]
                )
            }

            GLDrawerMesh.draw(
                drawer.drawerMesh,
                shaderModel
            )

            for (i in drawer.material.indices) {
                drawer.material[i].unbind(
                    shaderMaterial[i]
                )
            }
        }
    }
}