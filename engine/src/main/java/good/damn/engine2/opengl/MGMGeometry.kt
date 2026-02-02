package good.damn.engine2.opengl

import good.damn.apigl.drawers.GLDrawerMeshInstanced
import good.damn.apigl.drawers.GLDrawerMeshMaterialMutable
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.shaders.GLShaderGeometryPassInstanced
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.engine2.logic.MGMGeometryFrustrumMesh
import good.damn.engine2.opengl.models.MGMMeshDrawer
import java.util.concurrent.ConcurrentLinkedQueue

data class MGMGeometry(
    val meshes: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            GLShaderGeometryPassModel,
            MGMGeometryFrustrumMesh
        >
    >,
    val meshesInstanced: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            GLShaderGeometryPassInstanced,
            GLDrawerMeshInstanced
            >
    >,
    val meshSky: MGSky,
    val drawerQuad: GLDrawerVertexArray,
    val drawerSphere: GLDrawerVertexArray
) {

    inline fun drawMeshes() {
        meshes.forEach {
            if (!it.drawer.isOn) {
                return@forEach
            }

            it.shader.apply {
                use()
                it.drawer.drawer.drawNormals(
                    this
                )
                it.drawer.drawer.drawMaterials(
                    materials,
                    this
                )
            }
        }
    }

    inline fun drawMeshesInstanced() {
        meshesInstanced.forEach {
            it.shader.apply {
                use()
                it.drawer.draw(
                    materials
                )
            }
        }
    }

    inline fun draw() {
        meshSky.draw()
        drawMeshes()
        drawMeshesInstanced()
    }
}