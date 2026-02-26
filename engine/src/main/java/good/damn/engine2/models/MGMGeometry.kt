package good.damn.engine2.models

import good.damn.apigl.drawers.GLDrawerMeshInstanced
import good.damn.apigl.drawers.GLDrawerMeshMaterial
import good.damn.apigl.drawers.GLDrawerMeshMaterialNormals
import good.damn.apigl.drawers.GLDrawerNormalMatrix
import good.damn.apigl.shaders.GLShaderGeometryPassInstanced
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.engine2.logic.MGMGeometryFrustrumMesh
import good.damn.engine2.models.MGSky
import java.util.concurrent.ConcurrentLinkedQueue

data class MGMGeometry(
    val meshes: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            GLShaderGeometryPassModel,
            MGMGeometryFrustrumMesh<
                GLDrawerMeshMaterial
                >
            >
        >,
    val meshesNormals: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            GLShaderGeometryPassModel,
            MGMGeometryFrustrumMesh<
                GLDrawerMeshMaterialNormals
                >
            >
        >,
    val meshesInstanced: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            GLShaderGeometryPassInstanced,
            GLDrawerMeshInstanced
            >
        >,
    val meshSky: MGSky
) {

    inline fun drawMeshes() {
        // draw dynamic meshes without normal matrix
        meshes.forEach {
            if (!it.drawer.isOn) {
                return@forEach
            }

            it.shader.apply {
                use()
                GLDrawerMeshMaterial.Companion.draw(
                    it.drawer.drawer,
                    materials,
                    this
                )
            }
        }

        // draw dynamic meshes with normal matrix
        meshesNormals.forEach {
            if (!it.drawer.isOn) {
                return@forEach
            }

            it.shader.apply {
                use()

                GLDrawerNormalMatrix.Companion.draw(
                    it.drawer.drawer.normals,
                    this
                )

                GLDrawerMeshMaterial.Companion.draw(
                    it.drawer.drawer.meshMaterial,
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