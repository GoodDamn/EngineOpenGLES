package good.damn.engine.models

import good.damn.common.COHandlerGl
import good.damn.common.camera.COMCamera
import good.damn.common.volume.COManagerFrustrum
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.drawers.volume.MGDrawerVolumes
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.managers.MGDrawerLights
import good.damn.logic.triggers.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.models.MGMMeshDrawer
import good.damn.logic.pools.MGPoolMaterials
import good.damn.logic.pools.MGPoolMeshesStatic
import good.damn.logic.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.logic.process.MGManagerProcessTime
import java.util.concurrent.ConcurrentLinkedQueue

data class MGMInformator(
    val shaders: MGMInformatorShader,
    val camera: COMCamera,
    val drawerLightDirectional: MGDrawerLightDirectional,
    val drawerQuad: MGDrawerVertexArray,
    val drawerVolumes: MGDrawerVolumes,
    val meshes: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            MGShaderGeometryPassModel,
            MGDrawerMeshMaterialMutable
        >
    >,
    val meshesInstanced: ConcurrentLinkedQueue<
        MGMMeshDrawer<
            MGShaderGeometryPassInstanced,
            MGDrawerMeshInstanced
        >
    >,

    val framebufferG: MGFrameBufferG,

    val meshSky: MGSky,

    val managerLight: MGDrawerLights,
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
        MGShaderGeometryPassModel,
        MGDrawerMeshMaterialMutable
    >? = null
}