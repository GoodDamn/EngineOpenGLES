package good.damn.engine.models

import good.damn.common.COHandlerGl
import good.damn.common.camera.COMCamera
import good.damn.common.volume.COManagerFrustrum
import good.damn.apigl.drawers.MGDrawerLightDirectional
import good.damn.apigl.drawers.MGDrawerMeshMaterialMutable
import good.damn.apigl.drawers.MGDrawerVertexArray
import good.damn.apigl.drawers.MGDrawerMeshInstanced
import good.damn.apigl.drawers.MGDrawerVolumes
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.apigl.drawers.MGDrawerLights
import good.damn.logic.triggers.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.models.MGMMeshDrawer
import good.damn.logic.pools.MGPoolMaterials
import good.damn.logic.pools.MGPoolMeshesStatic
import good.damn.logic.pools.MGPoolTextures
import good.damn.apigl.shaders.MGShaderGeometryPassInstanced
import good.damn.apigl.shaders.MGShaderGeometryPassModel
import good.damn.logic.process.MGManagerProcessTime
import java.util.concurrent.ConcurrentLinkedQueue

data class MGMInformator(
    val shaders: MGMInformatorShader,
    val camera: COMCamera,
    val drawerLightDirectional: good.damn.apigl.drawers.MGDrawerLightDirectional,
    val drawerQuad: good.damn.apigl.drawers.MGDrawerVertexArray,
    val drawerVolumes: good.damn.apigl.drawers.MGDrawerVolumes,
    val meshes: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            good.damn.apigl.shaders.MGShaderGeometryPassModel,
            good.damn.apigl.drawers.MGDrawerMeshMaterialMutable
        >
    >,
    val meshesInstanced: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            good.damn.apigl.shaders.MGShaderGeometryPassInstanced,
            good.damn.apigl.drawers.MGDrawerMeshInstanced
        >
    >,

    val framebufferG: MGFrameBufferG,

    val meshSky: MGSky,

    val managerLight: good.damn.apigl.drawers.MGDrawerLights,
    val managerLightVolumes: COManagerFrustrum,
    val managerTrigger: good.damn.logic.triggers.managers.MGManagerTriggerMesh,
    val managerProcessTime: good.damn.logic.process.MGManagerProcessTime,

    val poolTextures: good.damn.logic.pools.MGPoolTextures,
    val poolMeshes: good.damn.logic.pools.MGPoolMeshesStatic,
    val poolMaterials: good.damn.logic.pools.MGPoolMaterials,

    val glHandler: COHandlerGl,

    var canDrawTriggers: Boolean,
) {
    var currentEditMesh: MGMMeshDrawer<
        good.damn.apigl.shaders.MGShaderGeometryPassModel,
        good.damn.apigl.drawers.MGDrawerMeshMaterialMutable
    >? = null
}