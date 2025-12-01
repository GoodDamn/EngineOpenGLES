package good.damn.engine.models

import good.damn.engine.opengl.camera.MGCamera
import good.damn.engine.opengl.camera.MGCameraFree
import good.damn.engine.opengl.drawers.MGDrawerLightDirectional
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialSwitch
import good.damn.engine.opengl.drawers.MGDrawerMeshTextureSwitch
import good.damn.engine.opengl.drawers.instance.MGDrawerMeshInstanced
import good.damn.engine.opengl.entities.MGSky
import good.damn.engine.opengl.managers.MGManagerLight
import good.damn.engine.opengl.managers.MGManagerTriggerLight
import good.damn.engine.opengl.managers.MGManagerTriggerMesh
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.thread.MGHandlerGl
import java.util.concurrent.ConcurrentLinkedQueue

data class MGMInformator(
    val shaders: MGMInformatorShader,
    val camera: MGCameraFree,
    val drawerLightDirectional: MGDrawerLightDirectional,
    val meshes: ConcurrentLinkedQueue<MGDrawerMeshMaterialSwitch>,
    val meshesInstanced: ConcurrentLinkedQueue<MGDrawerMeshInstanced>,

    val meshSky: MGSky,

    val managerLight: MGManagerLight,
    val managerTriggerLight: MGManagerTriggerLight,
    val managerTrigger: MGManagerTriggerMesh,

    val poolTextures: MGPoolTextures,
    val poolMeshes: MGPoolMeshesStatic,

    val glHandler: MGHandlerGl,

    var canDrawTriggers: Boolean
)