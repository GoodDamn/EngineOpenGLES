package good.damn.apigl.drawers

import good.damn.apigl.shaders.GLIShaderModel
import good.damn.apigl.shaders.GLShaderMaterial

data class GLDrawerMeshMaterial(
    var material: Array<GLMaterial>,
    var mesh: GLDrawerMesh
) {
    companion object {
        @JvmStatic
        fun draw(
            drawer: GLDrawerMeshMaterial,
            shaderMaterial: Array<GLShaderMaterial>,
            shaderModel: GLIShaderModel
        ) {
            for (i in drawer.material.indices) {
                drawer.material[i].draw(
                    shaderMaterial[i]
                )
            }

            GLDrawerMesh.draw(
                drawer.mesh,
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