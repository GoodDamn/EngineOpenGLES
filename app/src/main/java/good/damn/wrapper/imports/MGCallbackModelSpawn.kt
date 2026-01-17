package good.damn.wrapper.imports

import good.damn.engine.models.MGMInformator
import good.damn.apigl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.models.MGMMeshDrawer
import good.damn.engine.MGObject3d
import good.damn.apigl.shaders.MGShaderGeometryPassModel
import good.damn.apigl.shaders.MGShaderMaterial
import good.damn.apigl.shaders.base.MGBinderAttribute
import good.damn.logic.MGITrigger
import good.damn.logic.MGMatrixTriggerMesh
import good.damn.logic.MGTriggerMesh
import good.damn.engine.shader.generators.MGMMaterialShader
import good.damn.wrapper.hud.bridges.APBridgeRayIntersect

class MGCallbackModelSpawn(
    private val bridgeRay: APBridgeRayIntersect,
    private val triggerAction: good.damn.logic.MGITrigger,
    private val informator: MGMInformator
) {

    private val mBinderAttr = good.damn.apigl.shaders.base.MGBinderAttribute.Builder()
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
                good.damn.logic.MGTriggerMesh.createFromMeshPool(
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
        mesh: good.damn.logic.MGTriggerMesh
    ) {
        addMesh(
            informator.shaders.cacheGeometryPass.loadOrGetFromCache(
                material.srcCodeMaterial,
                informator.shaders.source.vert,
                mBinderAttr,
                arrayOf(
                    good.damn.apigl.shaders.MGShaderMaterial(
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
        matrix: good.damn.logic.MGMatrixTriggerMesh
    ) {
        bridgeRay.intersectUpdate = good.damn.wrapper.hud.bridges.APRayIntersectImplModel(
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
        shader: good.damn.apigl.shaders.MGShaderGeometryPassModel,
        material: MGMMaterialShader,
        mesh: good.damn.logic.MGTriggerMesh
    ) {
        val meshMaterial = MGMMeshDrawer(
            shader,
            good.damn.apigl.drawers.MGDrawerMeshMaterialMutable(
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