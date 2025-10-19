#include <assimp/DefaultIOStream.h>
#include <assimp/Importer.hpp>
#include <assimp/postprocess.h>
#include <assimp/scene.h>
#include <android/log.h>
#include <list>
#include <jni.h>

const char* TAG = "MGObject3d.cpp";
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

struct MGTexture {
    std::string fileName;
};

struct MGMesh {
    unsigned int numVertices;
    unsigned int numIndices;
    unsigned int numTextures = 0;

    float* vertices;
    int* indices;
    MGTexture* textures = nullptr;
};

std::string jStringToStd(
    JNIEnv* env,
    jbyteArray inp
) {
    jsize length = env->GetArrayLength(
        inp
    );

    jbyte* pointerArray = env->GetByteArrayElements(
        inp,
        nullptr
    );

    std::string output = std::string(
        reinterpret_cast<char*>(pointerArray),
        length
    );

    return output;
}

// MGObject3d
MGMesh* processMesh(
    aiMesh* mesh
) {
    unsigned int lenVerts = mesh->mNumVertices * 3;
    if (mesh->mNormals) {
        lenVerts += lenVerts;
    }

    if (mesh->mTextureCoords[0]) {
        lenVerts += mesh->mNumVertices * 2;
    }

    LOGD("VERTICES: %i NUM: %i ", lenVerts, mesh->mNumVertices);

    float *bufferVert = new float[lenVerts];
    unsigned int position = 0;

    for (int iVert = 0; iVert < mesh->mNumVertices; iVert++) {
        //LOGD("VERT: %i: %i", iVert, position);
        aiVector3D& v = mesh->mVertices[iVert];
        bufferVert[position++] = v.x;
        bufferVert[position++] = v.y;
        bufferVert[position++] = v.z;

        aiVector3D* vt = mesh->mTextureCoords[0];
        if (vt) {
            aiVector3D& vtt = vt[iVert];
            bufferVert[position++] = vtt.x;
            bufferVert[position++] = vtt.y;
        }

        if (mesh->mNormals) {
            aiVector3D& vn = mesh->mNormals[iVert];
            bufferVert[position++] = vn.x;
            bufferVert[position++] = vn.y;
            bufferVert[position++] = vn.z;
        }
    }

    position = 0;
    unsigned int lenIndices = mesh->mNumFaces * 3;

    LOGD("indices: %i", lenIndices);
    auto* indices = new int[lenIndices];
    for (int index = 0; index < mesh->mNumFaces; index++) {
        const aiFace& face = mesh->mFaces[index];
        for (int i = 0; i < face.mNumIndices; i++) {
            indices[position++] = face.mIndices[i];
        }
    }

    auto* meshOut = new MGMesh;
    meshOut->indices = indices;
    meshOut->vertices = bufferVert;
    meshOut->numIndices = lenIndices;
    meshOut->numVertices = lenVerts;

    return meshOut;
}

void loadTexturesTo(
    MGMesh* mesh,
    aiMaterial* material,
    aiTextureType type
) {
    unsigned int countTexture = material->GetTextureCount(
        type
    );

    if (countTexture == 0) {
        return;
    }

    auto* textures = new MGTexture[
        countTexture
    ];
    mesh->numTextures = countTexture;
    mesh->textures = textures;

    for (unsigned int i = 0; i < countTexture; i++) {
        aiString fileName;
        material->GetTexture(
            type,
            i,
            &fileName
        );

        std::string initStr = std::string(
            fileName.C_Str()
        );

        std::size_t pos = initStr.find_last_not_of("//") + 1;
        std::size_t posWin = initStr.find_last_of("/\\") + 1;

        LOGD("TEXTURE %i: POS: %i, POS_WIN: %i FOR %s", i, pos, posWin, fileName.C_Str());

        if (pos == -1 && posWin == -1) {
            textures[i].fileName = initStr;
            continue;
        }

        if (posWin != -1) {
            textures[i].fileName = initStr.substr(
                posWin
            );
            continue;
        }

        textures[i].fileName = initStr.substr(
            pos
        );
    }
}

void processNode(
    aiNode* node,
    const aiScene* scene,
    std::list<MGMesh*>& list
) {
    for (unsigned int i = 0; i < node->mNumMeshes; i++) {
        aiMesh* mesh = scene->mMeshes[
            node->mMeshes[i]
        ];

        MGMesh* outMesh = processMesh(
            mesh
        );

        list.push_back(
            outMesh
        );

        if (mesh->mMaterialIndex >= 0) {
            aiMaterial* material = scene->mMaterials[
                mesh->mMaterialIndex
            ];
            loadTexturesTo(
                outMesh,
                material,
                aiTextureType_DIFFUSE
            );
        }
    }

    for (unsigned int i = 0; i < node->mNumChildren; i++) {
        processNode(
            node->mChildren[i],
            scene,
            list
        );
    }
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_good_damn_engine_opengl_MGObject3d_createFromPath(
    JNIEnv *env,
    jclass clazz,
    jbyteArray path
) {
    std::string jPath = jStringToStd(
        env,
        path
    );
    const char* jPathC = jPath.c_str();

    LOGD("%s", jPathC);

    Assimp::Importer importer;

    LOGD("set IO handler");

    const aiScene* scene = importer.ReadFile(
        jPath,
        aiProcess_Triangulate |
        aiProcess_JoinIdenticalVertices |
        aiProcess_FlipUVs
    );

    if (!scene) {
        LOGD("%s", importer.GetErrorString());
        return nullptr;
    }

    aiNode* rootNode = scene->mRootNode;

    LOGD("scene loaded");

    if (scene->mNumMeshes == 0) {
        return nullptr;
    }

    jclass classElement = env->FindClass(
        "good/damn/engine/opengl/MGObject3d"
    );

    std::list<MGMesh*> meshes;

    processNode(
        rootNode,
        scene,
        meshes
    );
    LOGD("NODE PROCESSED");

    jmethodID constructorElement = env->GetMethodID(
        classElement,
        "<init>",
        "([F[I[Ljava/lang/String;)V"
    );

    jobjectArray arrayObject = env->NewObjectArray(
        scene->mNumMeshes,
        classElement,
        nullptr
    );

    jclass classString = env->FindClass(
        "java/lang/String"
    );

    // Copy content to java arrays
    unsigned int size = meshes.size();
    for (int i = 0; i < size; i++) {
        LOGD("COPY: %i", i);
        auto iteratorBegin = meshes.begin();
        std::advance(iteratorBegin, i);
        MGMesh* mesh = *iteratorBegin;

        jfloatArray arrVert = env->NewFloatArray(
            mesh->numVertices
        );

        env->SetFloatArrayRegion(
            arrVert,
            0,
            mesh->numVertices,
            mesh->vertices
        );

        delete[] mesh->vertices;

        jintArray arrIndices = env->NewIntArray(
            mesh->numIndices
        );

        env->SetIntArrayRegion(
            arrIndices,
            0,
            mesh->numIndices,
            mesh->indices
        );

        delete[] mesh->indices;

        jobjectArray arrFileName = nullptr;
        if (mesh->textures) {
            arrFileName = env->NewObjectArray(
                mesh->numTextures,
                classString,
                nullptr
            );

            for (
                unsigned int textureIndex = 0;
                textureIndex < mesh->numTextures;
                textureIndex++
            ) {
                LOGD("TEXTURE_JNI: %i->%s", textureIndex, mesh->textures[textureIndex].fileName.c_str());
                env->SetObjectArrayElement(
                    arrFileName,
                    textureIndex,
                    env->NewStringUTF(
                        mesh->textures[textureIndex].fileName.c_str()
                    )
                );
            }

            delete[] mesh->textures;
        }

        jobject obj = env->NewObject(
            classElement,
            constructorElement,
            arrVert,
            arrIndices,
            arrFileName
        );

        env->SetObjectArrayElement(
            arrayObject,
            i,
            obj
        );

        delete mesh;
    }

    return arrayObject;
}
