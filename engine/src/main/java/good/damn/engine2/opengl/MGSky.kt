package good.damn.engine2.opengl

import android.opengl.GLES30
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
import good.damn.apigl.drawers.GLDrawerMaterialTexture
import good.damn.apigl.drawers.GLDrawerMesh
import good.damn.apigl.drawers.GLDrawerMeshMaterialMutable
import good.damn.apigl.drawers.GLDrawerPositionEntity
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.enums.GLEnumArrayVertexConfiguration
import good.damn.apigl.shaders.GLShaderGeometryPassModel
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.common.matrices.COMatrixScale
import good.damn.engine2.opengl.models.MGMMeshDrawer
import good.damn.engine.ASObject3d
import good.damn.engine2.models.MGMInformatorShader
import good.damn.engine2.opengl.pools.MGPoolTextures
import good.damn.engine2.shader.generators.MGMMaterialShader

class MGSky {
    private lateinit var meshMaterial: MGMMeshDrawer<
        GLShaderGeometryPassModel,
        GLDrawerMeshMaterialMutable
        >

    fun configure(
        shaders: MGMInformatorShader,
        poolTextures: MGPoolTextures
    ) {
        val verticesSky = GLArrayVertexConfigurator(
            GLEnumArrayVertexConfiguration.SHORT
        )

        ASObject3d.createFromAssets(
            "objs/semi_sphere.obj"
        )?.get(0)?.apply {
            verticesSky.configure(
                vertices,
                indices,
                GLPointerAttribute.defaultNoTangent
            )
        }

        val localDirPath = "textures/sky"
        val materialShader = MGMMaterialShader.Builder(
            "sky",
            localDirPath,
            shaders.source
        ).diffuse()
            .opacity()
            .emissive(1.0f)
            .normal()
            .useDepthConstant()
            .specular()
            .build()

        materialShader.loadTextures(
            poolTextures,
            localDirPath
        )

        val shader = shaders.cacheGeometryPass.loadOrGetFromCache(
            materialShader.srcCodeMaterial,
            shaders.source.vert,
            GLBinderAttribute.Builder()
                .bindPosition()
                .bindTextureCoordinates()
                .build(),
            arrayOf(
                GLShaderMaterial(
                    materialShader.shaderTextures
                )
            )
        )

        meshMaterial = MGMMeshDrawer(
            shader,
            GLDrawerMeshMaterialMutable(
                arrayOf(
                    GLMaterial(
                        GLDrawerMaterialTexture(
                            materialShader.textures
                        )
                    )
                ),
                GLDrawerMesh(
                    GLDrawerVertexArray(
                        verticesSky
                    ),
                    GLDrawerPositionEntity(
                        COMatrixScale().apply {
                            setScale(
                                2000000f,
                                2000000f,
                                2000000f
                            )
                            invalidateScale()
                        }
                    ),
                    GLES30.GL_CW
                )
            )
        )
    }

    fun draw() {
        meshMaterial.shader.apply {
            use()
            meshMaterial.drawer.drawMaterials(
                materials,
                this
            )
        }
    }
}