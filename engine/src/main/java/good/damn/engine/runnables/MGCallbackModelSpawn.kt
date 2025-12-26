package good.damn.engine.runnables

import good.damn.engine.models.MGMInformator
import good.damn.engine.opengl.bridges.MGBridgeRayIntersect
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.models.MGMMeshMaterial
import good.damn.engine.opengl.models.MGMPoolMeshMutable
import good.damn.engine.opengl.models.MGMPoolVertexArray
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel
import good.damn.engine.opengl.triggers.MGIMatrixTrigger
import good.damn.engine.opengl.triggers.MGITrigger
import good.damn.engine.opengl.triggers.MGTriggerMesh
import good.damn.engine.shader.generators.MGMMaterialShader

class MGCallbackModelSpawn(
    private val bridgeRay: MGBridgeRayIntersect,
    private val triggerAction: MGITrigger,
    private val informator: MGMInformator
): MGICallbackModel {

    override fun onGetObjectsCached(
        poolMesh: Array<MGMPoolVertexArray>
    ) {
        if (poolMesh.isEmpty()) {
            return
        }

        if (poolMesh.size == 1) {
            processMesh(
                getCachedMaterial(
                    null
                ),
                MGTriggerMesh.createFromMeshPool(
                    poolMesh[0],
                    triggerAction
                )
            )
            return
        }

        /*processGroupMesh(
            MGTriggerMeshGroup.createFromPool(
                poolMesh,
                triggerAction
            )
        )*/
    }

    override fun onGetObjects(
        fileName: String,
        objs: Array<MGObject3d>?
    ) {
        objs ?: return
        if (objs.isEmpty()) {
            return
        }

        if (objs.size == 1) {
            val outPoolMesh = MGMPoolMeshMutable()
            MGTriggerMesh.createFromObject(
                objs[0],
                informator,
                outPoolMesh,
                triggerAction,
            ).run {
                informator.poolMeshes[fileName] = arrayOf(
                    outPoolMesh.toImmutable()
                )

                processMesh(
                    getCachedMaterial(
                        null
                    ),
                    this
                )
            }
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
            MGMaterial.generateShaderModel(
                material,
                informator
            ),
            material,
            mesh
        )
        setupMatrix(
            mesh.matrix
        )
    }

    private inline fun getCachedMaterial(
        fileNameDiffuse: String?
    ): MGMMaterialShader {
        val poolMaterials = informator
            .poolMaterials

        if (fileNameDiffuse == null) {
            return poolMaterials.default
        }

        val cachedMaterial = poolMaterials[
            fileNameDiffuse
        ]

        if (cachedMaterial != null) {
            return cachedMaterial
        }

        return MGMMaterialShader.Builder(
            fileNameDiffuse,
            "textures",
            informator.shaders.source
        ).diffuse()
            .opacity()
            .emissive(0.0f)
            .normal()
            .specular()
            .useDepthFunc()
            .build()
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
        matrix: MGIMatrixTrigger
    ) {
        bridgeRay.matrix = matrix
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
        informator.meshes.add(
            MGMMeshMaterial(
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
        )

        informator.managerTrigger.addTrigger(
            mesh.triggerState
        )
    }
}