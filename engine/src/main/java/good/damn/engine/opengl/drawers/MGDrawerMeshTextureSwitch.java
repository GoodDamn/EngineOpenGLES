package good.damn.engine.opengl.drawers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.enums.MGEnumDrawMode;
import good.damn.engine.opengl.enums.MGEnumTextureType;
import good.damn.engine.opengl.shaders.MGIShaderModel;
import good.damn.engine.opengl.shaders.MGIShaderNormal;
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.textures.MGTexture;

public class MGDrawerMeshTextureSwitch {

    @NonNull
    private final MGMaterial[] materials;

    @NonNull
    protected final MGDrawerMeshSwitch mDrawerMesh;

    @NonNull
    private MGIDrawerTexture<
        MGIShaderTextureUniform
    >[] mDrawerTextures;

    public MGDrawerMeshTextureSwitch(
        @NonNull final MGMaterial[] materials,
        @NonNull final MGDrawerMeshSwitch drawerMesh
    ) {
        this.materials = materials;
        mDrawerMesh = drawerMesh;
        mDrawerTextures = new MGIDrawerTexture[
            materials.length
        ];

        switchTextures(
            MGEnumTextureType.DIFFUSE
        );
    }

    public final void switchDrawMode(
        @NonNull final MGEnumDrawMode drawMode
    ) {
        switch (drawMode) {
            case OPAQUE:
            case DIFFUSE:
                switchTextures(
                    MGEnumTextureType.DIFFUSE
                );
                break;
            case METALLIC:
                switchTextures(
                    MGEnumTextureType.METALLIC
                );
                break;
            case EMISSIVE:
                switchTextures(
                    MGEnumTextureType.EMISSIVE
                );
                break;
            case NORMAL_MAP:
                switchTextures(
                    MGEnumTextureType.NORMAL
                );
                break;
        }

        mDrawerMesh.switchDrawMode(
            drawMode
        );
    }

    public final void drawNormals(
        @NonNull final MGIShaderNormal shader
    ) {
        mDrawerMesh.drawNormals(
            shader
        );
    }

    public final void drawSingleTexture(
        @NonNull final MGIShaderTextureUniform shaderTexture,
        @NonNull final MGIShaderModel shaderModel
    ) {
        for (
            @Nullable final MGIDrawerTexture<
                MGIShaderTextureUniform
            > drawer: mDrawerTextures
        ) {
            if (drawer == null) {
                continue;
            }
            drawer.draw(shaderTexture);
        }

        mDrawerMesh.draw(
            shaderModel
        );

        for (
            @Nullable final MGIDrawerTexture<
                MGIShaderTextureUniform
                > drawer: mDrawerTextures
        ) {
            if (drawer == null) {
                continue;
            }
            drawer.unbind(shaderTexture);
        }
    }

    public final void drawVertices(
        @NonNull final MGIShaderModel shader
    ) {
        mDrawerMesh.draw(
            shader
        );
    }

    private final void switchTextures(
        @NonNull final MGEnumTextureType type
    ) {
        for (
            byte i = 0;
            i < mDrawerTextures.length;
            i++
        ) {
            mDrawerTextures[i] = materials[i].getTextureByType(
                type
            );
        }
    }
}
