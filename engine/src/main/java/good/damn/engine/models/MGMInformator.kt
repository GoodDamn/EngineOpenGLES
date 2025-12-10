package good.damn.engine.models

import good.damn.engine.loaders.texture.MGLoaderTextureAsync
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerLightPass
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderOpaqueSingle
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.shaders.MGShaderOpaqueDefer
import good.damn.engine.opengl.thread.MGHandlerGl
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

data class MGMInformator(
    val shaders: MGMInformatorShader,
    val camera: MGCameraFree,
    val drawerLightDirectional: MGDrawerLightDirectional,
    val drawerLightPass: MGDrawerLightPass,
    val meshes: ConcurrentHashMap<
        MGShaderOpaqueSingle,
        ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>
    >,

    val meshesInstanced: ConcurrentHashMap<
        MGShaderOpaqueDefer,
        ConcurrentLinkedQueue<MGDrawerMeshInstanced>
    >,

    val meshSky: MGSky,

    val managerLight: MGManagerLight,
    val managerTriggerLight: MGManagerTriggerLight,
    val managerTrigger: MGManagerTriggerMesh,

    val poolTextures: MGPoolTextures,
    val poolMeshes: MGPoolMeshesStatic,

    val glHandler: MGHandlerGl,
    val glLoaderTexture: MGLoaderTextureAsync,

    var canDrawTriggers: Boolean
)