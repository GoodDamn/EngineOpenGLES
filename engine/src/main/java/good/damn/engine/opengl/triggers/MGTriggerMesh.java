package good.damn.engine.opengl.triggers;

import android.opengl.GLES30;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.LinkedList;

import good.damn.engine.loaders.MGLoaderLevelLibrary;
import good.damn.engine.loaders.texture.MGLoaderTextureAsync;
import good.damn.engine.models.MGMInformator;
import good.damn.engine.models.MGMInformatorShader;
import good.damn.engine.opengl.arrays.MGArrayVertexManager;
import good.damn.engine.opengl.arrays.pointers.MGPointerAttribute;
import good.damn.engine.opengl.drawers.MGDrawerVertexArray;
import good.damn.engine.opengl.entities.MGMaterialTexture;
import good.damn.engine.opengl.enums.MGEnumTextureType;
import good.damn.engine.opengl.objects.MGObject3d;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.shaders.MGShaderGeometryPassModel;
import good.damn.engine.opengl.shaders.MGShaderTexture;
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute;
import good.damn.engine.sdk.SDVector3;
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterial;
import good.damn.engine.opengl.drawers.MGDrawerMeshSwitchNormals;
import good.damn.engine.opengl.drawers.MGDrawerPositionEntity;
import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation;
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert;
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal;
import good.damn.engine.opengl.models.MGMPoolMesh;
import good.damn.engine.opengl.models.MGMPoolMeshMutable;
import good.damn.engine.opengl.thread.MGHandlerGl;
import good.damn.engine.opengl.triggers.callbacks.MGManagerTriggerStateCallback;
import good.damn.engine.opengl.triggers.methods.MGTriggerMethodBox;
import good.damn.engine.opengl.triggers.stateables.MGDrawerTriggerStateable;
import good.damn.engine.runnables.MGRunnableConfigVertexArray;
import good.damn.engine.shader.MGShaderCache;
import good.damn.engine.shader.MGShaderSource;
import good.damn.engine.shader.generators.MGGeneratorMaterialG;
import good.damn.engine.utils.MGUtilsAlgo;

public final class MGTriggerMesh {

    @NonNull
    public final MGMatrixTriggerMesh matrix;

    @NonNull
    public final MGDrawerMeshMaterial mesh;

    @NonNull
    public final MGDrawerTriggerStateable triggerState;

    @NonNull
    public final MGShaderGeometryPassModel shaderOpaque;

    private MGTriggerMesh(
        @NonNull final MGMatrixTriggerMesh matrix,
        @NonNull final MGDrawerMeshMaterial mesh,
        @NonNull final MGDrawerTriggerStateable triggerState,
        @NonNull final MGShaderGeometryPassModel shaderOpaque
    ) {
        this.matrix = matrix;
        this.mesh = mesh;
        this.triggerState = triggerState;
        this.shaderOpaque = shaderOpaque;
    }

    @NonNull
    public static MGTriggerMesh createFromObject(
        @NonNull final MGObject3d obj,
        @NonNull final MGMInformator informator,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull
        final MGArrayVertexManager arrayVertex = new MGArrayVertexManager(
            obj.config
        );

        @NonNull
        final MGHandlerGl glHandler = informator
            .getGlHandler();

        @NonNull
        final MGLoaderTextureAsync loaderTexture = informator
            .getGlLoaderTexture();

        @NonNull
        final MGMInformatorShader shaders = informator
            .getShaders();

        @NonNull
        final MGShaderCache<
            MGShaderGeometryPassModel
        > shaderCache = shaders.getOpaqueGenerated();

        @NonNull
        final MGShaderSource shaderSource = shaders
            .getSource();

        glHandler.post(
            new MGRunnableConfigVertexArray(
                arrayVertex,
                obj.vertices,
                obj.indices,
                MGPointerAttribute.defaultNoTangent
            )
        );

        arrayVertex.keepBufferVertices(
            obj.vertices
        );

        @NonNull
        final MGMaterialTexture.Builder builder = new MGMaterialTexture.Builder();

        @NonNull
        final MGGeneratorMaterialG generatorMaterial = new MGGeneratorMaterialG(
            shaderSource
        );

        @NonNull
        final LinkedList<
            MGShaderTexture
        > shaderTextures = new LinkedList<>();

        if (obj.texturesDiffuseFileName == null) {
            generatorMaterial.componeEntity(
                shaderSource.getFragDeferDiffuseNo()
            );
        } else {
            generatorMaterial.componeEntity(
                shaderSource.getFragDeferDiffuse()
            );

            builder.buildTexture(
                obj.texturesDiffuseFileName[0],
                MGEnumTextureType.DIFFUSE
            );

            shaderTextures.add(
                new MGShaderTexture(
                    MGLoaderLevelLibrary.ID_DIFFUSE
                )
            );
        }

        if (obj.texturesMetallicFileName == null) {
            generatorMaterial.componeEntity(
                shaderSource.getFragDeferSpecularNo()
            );
        } else {
            shaderTextures.add(
                new MGShaderTexture(
                    MGLoaderLevelLibrary.ID_METALLIC
                )
            );
            builder.buildTexture(
                obj.texturesMetallicFileName[0],
                MGEnumTextureType.METALLIC
            );
            generatorMaterial.componeEntity(
                shaderSource.getFragDeferSpecular()
            );
        }

        generatorMaterial.componeEntity(
            shaderSource.getFragDeferOpacityNo()
        );

        if (obj.texturesEmissiveFileName == null) {
            generatorMaterial.componeEntity(
                shaderSource.getFragDeferEmissiveNo()
            );
        } else {
            shaderTextures.add(
                new MGShaderTexture(
                    MGLoaderLevelLibrary.ID_EMISSIVE
                )
            );
            builder.buildTexture(
                obj.texturesEmissiveFileName[0],
                MGEnumTextureType.EMISSIVE
            );
            generatorMaterial.componeEntity(
                shaderSource.getFragDeferEmissive()
            );
        }

        generatorMaterial.componeEntity(
            shaderSource.getFragDeferNormalVertex()
        );

        @NonNull
        final String src = generatorMaterial.build();

        @NonNull
        MGShaderGeometryPassModel cachedShader = shaderCache.get(
            src
        );

        if (cachedShader == null) {
            cachedShader = new MGShaderGeometryPassModel(
                MGShaderMaterial.singleMaterial(
                    shaderTextures.toArray(new MGShaderTexture[0])
                )
            );
            shaderCache.cacheAndCompile(
                src,
                shaderSource.getVert(),
                cachedShader,
                glHandler,
                new MGBinderAttribute.Builder()
                    .bindPosition()
                    .bindTextureCoordinates()
                    .bindNormal()
                    .build()
            );
        }

        @NonNull
        final MGMaterialTexture materialTexture = builder
            .build();

        materialTexture.load(
            informator.getPoolTextures(),
            "textures",
            loaderTexture
        );

        @NonNull
        final MGMaterial material = new MGMaterial(
            builder.build()
        );

        return createFromVertexArray(
            arrayVertex,
            material,
            outPoolMesh,
            triggerAction,
            cachedShader
        );
    }

    @NonNull
    public static MGTriggerMesh createFromVertexArray(
        @NonNull final MGArrayVertexManager vertexArray,
        @NonNull final MGMaterial material,
        @NonNull final MGMPoolMeshMutable outPoolMesh,
        @NonNull final MGITrigger triggerAction,
        @NonNull final MGShaderGeometryPassModel shaderOpaque
    ) {
        outPoolMesh.pointMinMax = MGUtilsAlgo.findMinMaxPoints(
            vertexArray
        );

        outPoolMesh.pointMiddle = outPoolMesh.pointMinMax.first.interpolate(
            outPoolMesh.pointMinMax.second,
            0.5f
        );

        MGUtilsAlgo.offsetAnchorPoint(
            vertexArray,
            outPoolMesh.pointMiddle
        );

        vertexArray.unkeepBufferVertices();

        outPoolMesh.material = material;
        outPoolMesh.vertexArray = vertexArray;
        outPoolMesh.shaderOpaque = shaderOpaque;

        return createFromMeshPool(
            outPoolMesh.toImmutable(),
            triggerAction
        );
    }

    @NonNull
    public static MGTriggerMesh createFromMeshPool(
        @NonNull final MGMPoolMesh poolMesh,
        @NonNull final MGITrigger triggerAction
    ) {
        @NonNull final Pair<
            SDVector3, SDVector3
        > pointMinMax = poolMesh.getPointMinMax();

        @NonNull final SDVector3 pointMiddle = poolMesh
            .getPointMiddle();

        @NonNull
        final MGMatrixTriggerMesh matrix = new MGMatrixTriggerMesh(
            new MGMatrixTransformationInvert<>(
                new MGMatrixScaleRotation()
            ),
            new MGMatrixTransformationNormal<>(
                new MGMatrixScaleRotation()
            ),
            pointMinMax.first,
            pointMinMax.second
        );

        matrix.setPosition(
            pointMiddle.getX(),
            pointMiddle.getY(),
            pointMiddle.getZ()
        );

        matrix.invalidatePosition();
        matrix.invalidateScaleRotation();
        matrix.calculateInvertTrigger();
        matrix.calculateNormals();

        @NonNull
        final MGMaterial material = poolMesh.getMaterial();

        @NonNull
        final MGDrawerMeshSwitchNormals drawerMeshSwitchNormals = new MGDrawerMeshSwitchNormals(
            new MGDrawerVertexArray(
                poolMesh.getVertexArray()
            ),
            new MGDrawerPositionEntity(
                matrix.matrixMesh.model
            ),
            GLES30.GL_CCW,
            matrix.matrixMesh.normal
        );

        @NonNull
        final MGDrawerMeshMaterial meshTexture = new MGDrawerMeshMaterial(
            new MGMaterial[]{material},
            drawerMeshSwitchNormals
        );

        @NonNull
        final MGDrawerTriggerStateable triggerState = new MGDrawerTriggerStateable(
            new MGManagerTriggerStateCallback(
                new MGTriggerMethodBox(
                    matrix.matrixTrigger.invert
                ),
                triggerAction
            ),
            matrix.matrixTrigger.model
        );

        return new MGTriggerMesh(
            matrix,
            meshTexture,
            triggerState,
            poolMesh.getShader()
        );
    }
}
