package good.damn.engine.opengl.drawers;

import androidx.annotation.NonNull;

import good.damn.engine.opengl.entities.MGMaterial;
import good.damn.engine.opengl.enums.MGEnumDrawMode;
import good.damn.engine.opengl.shaders.MGIShaderModel;
import good.damn.engine.opengl.shaders.MGIShaderNormal;
import good.damn.engine.opengl.shaders.MGIShaderTextureUniform;
import good.damn.engine.opengl.shaders.MGShaderMaterial;
import good.damn.engine.opengl.textures.MGTexture;

public class MGDrawerMeshTextureSwitch {

    @NonNull
    private final MGTexture mTextureDiffuse;

    @NonNull
    private final MGTexture mTextureMetallic;

    @NonNull
    private final MGTexture mTextureEmissive;

    @NonNull
    private final MGTexture mTextureNormal;

    @NonNull
    protected final MGDrawerMeshSwitch mDrawerMesh;

    @NonNull
    private MGIDrawerTexture<
        MGIShaderTextureUniform
    > mDrawerTexture;

    public MGDrawerMeshTextureSwitch(
        @NonNull final MGMaterial material,
        @NonNull final MGDrawerMeshSwitch drawerMesh
    ) {
        this(
            material.getTextureDiffuse(),
            material.getTextureMetallic(),
            material.getTextureEmissive(),
            material.getTextureNormal(),
            drawerMesh
        );
    }

    public MGDrawerMeshTextureSwitch(
        @NonNull final MGTexture textureDiffuse,
        @NonNull final MGTexture textureMetallic,
        @NonNull final MGTexture textureEmissive,
        @NonNull final MGTexture textureNormal,
        @NonNull final MGDrawerMeshSwitch drawerMesh
    ) {
        mTextureDiffuse = textureDiffuse;
        mTextureMetallic = textureMetallic;
        mTextureEmissive = textureEmissive;
        mTextureNormal = textureNormal;
        mDrawerMesh = drawerMesh;

        mDrawerTexture = textureDiffuse;
    }

    public final void switchDrawMode(
        @NonNull final MGEnumDrawMode drawMode
    ) {
        switch (drawMode) {
            case OPAQUE:
            case DIFFUSE:
                mDrawerTexture = mTextureDiffuse;
                break;
            case METALLIC:
                mDrawerTexture = mTextureMetallic;
                break;
            case EMISSIVE:
                mDrawerTexture = mTextureEmissive;
                break;
            case NORMAL_MAP:
                mDrawerTexture = mTextureNormal;
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
        mDrawerTexture.draw(
            shaderTexture
        );

        mDrawerMesh.draw(
            shaderModel
        );

        mDrawerTexture.unbind(
            shaderTexture
        );
    }

    public final void drawVertices(
        @NonNull final MGIShaderModel shader
    ) {
        mDrawerMesh.draw(
            shader
        );
    }
}
