package good.damn.wrapper.imports

import good.damn.apigl.drawers.GLDrawerMaterialTexture
import good.damn.apigl.drawers.GLDrawerMesh
import good.damn.apigl.drawers.GLDrawerMeshMaterial
import good.damn.apigl.drawers.GLDrawerMeshMaterialNormals
import good.damn.apigl.drawers.GLDrawerNormalMatrix
import good.damn.apigl.drawers.GLDrawerPositionEntity
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.enums.GLEnumFaceOrder
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.engine2.models.MGMMeshDrawer
import good.damn.engine.ASObject3d
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.engine2.logic.MGMGeometryFrustrumMesh
import good.damn.engine2.logic.MGVolumeTriggerMesh
import good.damn.engine2.shader.generators.MGMMaterialShader
import good.damn.logic.triggers.LGMatrixTriggerMesh
import good.damn.logic.triggers.LGTriggerMesh
import good.damn.wrapper.hud.bridges.APBridgeRayIntersect
import good.damn.wrapper.hud.bridges.APRayIntersectImplModel
import good.damn.engine2.providers.MGProviderGL

class APCallbackModelSpawn(
    private val bridgeRay: APBridgeRayIntersect
): MGProviderGL() {

    private val mBinderAttr = GLBinderAttribute.Builder()
        .bindPosition()
        .bindTextureCoordinates()
        .bindNormal()
        .build()

    fun processObjects(
        fileName: String,
        objs: Array<ASObject3d>?
    ) {
        objs ?: return
        if (objs.isEmpty()) {
            return
        }

        if (objs.size == 1) {
            val poolMesh = glProvider.pools.meshes.loadOrGetFromCache(
                fileName
            ) ?: return

            val triggerMatrix = LGTriggerMesh.createTriggerPointMatrix(
                poolMesh[0].triggerPoint
            )

            processMesh(
                MGMMaterialShader.getDefault(
                    glProvider.shaders.source
                ),
                LGTriggerMesh.createFromMatrix(
                    triggerMatrix
                ),
                poolMesh[0].drawerVertexArray
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
        mesh: LGTriggerMesh,
        drawerVertexArray: GLDrawerVertexArray
    ) {
        addMesh(
            glProvider.shaders.cacheGeometryPass.loadOrGetFromCache(
                material.srcCodeMaterial,
                glProvider.shaders.source.vert,
                mBinderAttr,
                arrayOf(
                    GLShaderMaterial(
                        material.shaderTextures
                    )
                )
            ),
            material,
            mesh,
            drawerVertexArray
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
        matrix: LGMatrixTriggerMesh
    ) {
        bridgeRay.intersectUpdate = APRayIntersectImplModel(
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
        shader: GLShaderGeometryPassModel,
        material: MGMMaterialShader,
        mesh: LGTriggerMesh,
        drawerVertexArray: GLDrawerVertexArray
    ) {
        val drawerMesh = GLDrawerMeshMaterialNormals(
            GLDrawerMeshMaterial(
                arrayOf(
                    GLMaterial(
                        GLDrawerMaterialTexture(
                            material.textures
                        )
                    )
                ),
                GLDrawerMesh(
                    drawerVertexArray,
                    GLDrawerPositionEntity(
                        mesh.matrix.matrixMesh.model
                    ),
                    GLEnumFaceOrder.COUNTER_CLOCK_WISE
                )
            ),
            GLDrawerNormalMatrix(
                mesh.matrix.matrixMesh.normal
            )
        )

        val frustrumMesh = MGMGeometryFrustrumMesh(
            false,
            drawerMesh
        )

        val meshMaterial = MGMMeshDrawer(
            shader,
            frustrumMesh
        )

        val volume = MGVolumeTriggerMesh(
            mesh.matrix.matrixTrigger.model,
            frustrumMesh
        )

        glProvider.apply {
            parameters.currentEditMesh = meshMaterial
            geometry.meshesNormals.add(
                meshMaterial
            )

            managers.managerFrustrum.volumes.add(
                volume
            )

            managers.managerTrigger.addTrigger(
                volume
            )
        }
    }
}