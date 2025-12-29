package good.damn.engine.models

import good.damn.engine.loaders.texture.MGLoaderTextureAsync
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerLightPass
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.drawers.MGDrawerVertexArray
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.framebuffer.MGFrameBufferG
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.models.MGMMeshMaterial
import good.damn.engine.opengl.pools.MGPoolMaterials
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderGeometryPassInstanced
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.runnables.MGManagerProcessTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

data class MGMInformator(
    val shaders: MGMInformatorShader,
    val camera: MGCameraFree,
    val drawerLightDirectional: MGDrawerLightDirectional,
    val drawerQuad: MGDrawerVertexArray,
    val meshes: ConcurrentLinkedQueue<
        MGMMeshMaterial
    >,

    val framebufferG: MGFrameBufferG,

    val meshesInstanced: ConcurrentHashMap<
        MGShaderGeometryPassInstanced,
        ConcurrentLinkedQueue<MGDrawerMeshInstanced>
    >,

    val meshSky: MGSky,

    val managerLight: MGManagerLight,
    val managerTriggerLight: MGManagerTriggerLight,
    val managerTrigger: MGManagerTriggerMesh,
    val managerProcessTime: MGManagerProcessTime,

    val poolTextures: MGPoolTextures,
    val poolMeshes: MGPoolMeshesStatic,
    val poolMaterials: MGPoolMaterials,

    val glHandler: MGHandlerGl,

    var canDrawTriggers: Boolean,
) {
    var currentEditMesh: MGMMeshMaterial? = null
}