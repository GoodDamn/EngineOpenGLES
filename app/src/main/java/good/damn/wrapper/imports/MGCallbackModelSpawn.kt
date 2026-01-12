package good.damn.wrapper.imports

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.models.MGMMeshDrawer
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh
import good.damn.engine.opengl.triggers.MGTriggerMesh
import good.damn.engine.shader.generators.MGMMaterialShader
import good.damn.wrapper.hud.bridges.MGBridgeRayIntersect

class MGCallbackModelSpawn(
    private val bridgeRay: MGBridgeRayIntersect,
    private val triggerAction: MGITrigger,
    private val informator: MGMInformator
) {

    private val mBinderAttr = MGBinderAttribute.Builder()
        .bindPosition()
        .bindTextureCoordinates()
        .bindNormal()
        .build()

    fun processObjects(
        fileName: String,
        objs: Array<MGObject3d>?
    ) {
        objs ?: return
        if (objs.isEmpty()) {
            return
        }

        if (objs.size == 1) {
            val poolMesh = informator.poolMeshes.loadOrGetFromCache(
                fileName,
                informator
            ) ?: return

            processMesh(
                MGMMaterialShader.getDefault(
                    informator.shaders.source
                ),
                MGTriggerMesh.createFromMeshPool(
                    poolMesh[0],
                    triggerAction
                )
            )

            return
        }

        /*val outPoolMeshes = Array(
            objs.size
        ) { MGMPoolMeshMutable() }

        MGTriggerMeshGroup.createFromObjects(
            objs,
            outPoolMeshes,
            triggerAction,
            informator
        ).run {
            informator.poolMeshes[fileName] = Array(
                outPoolMeshes.size
            ) { outPoolMeshes[it].toImmutable() }

            processGroupMesh(
                this
            )
        }*/
    }

    private inline fun processMesh(
        material: MGMMaterialShader,
        mesh: MGTriggerMesh
    ) {
        addMesh(
            informator.shaders.cacheGeometryPass.loadOrGetFromCache(
                material.srcCodeMaterial,
                informator.shaders.source.vert,
                mBinderAttr,
                arrayOf(
                    MGShaderMaterial(
                        material.shaderTextures
                    )
                )
            ),
            material,
            mesh
        )
        setupMatrix(
            mesh.matrix
        )
    }

    /*private fun processGroupMesh(
        meshGroup: MGTriggerMeshGroup
    ) {
        meshGroup.meshes.forEach {
            addMesh(it)
        }

        setupMatrix(
            meshGroup.matrix
        )
    }*/

    private inline fun setupMatrix(
        matrix: MGMatrixTriggerMesh
    ) {
        bridgeRay.intersectUpdate = good.damn.wrapper.hud.bridges.MGRayIntersectImplModel(
            matrix
        )

        matrix.run {
            addPosition(
                bridgeRay.outPointLead.x,
                bridgeRay.outPointLead.y,
                bridgeRay.outPointLead.z
            )
            invalidateScaleRotation()
            invalidatePosition()

            calculateInvertTrigger()
            calculateNormals()
        }
    }

    private inline fun addMesh(
        shader: MGShaderGeometryPassModel,
        material: MGMMaterialShader,
        mesh: MGTriggerMesh
    ) {
        val meshMaterial = MGMMeshDrawer(
            shader,
            MGDrawerMeshMaterialMutable(
                arrayOf(
                    MGMaterial(
                        material.materialTexture
                    )
                ),
                mesh.mesh
            )
        )
        informator.currentEditMesh = meshMaterial

        informator.meshes.add(
            meshMaterial
        )

        informator.managerTrigger.addTrigger(
            mesh.triggerState
        )
    }
}