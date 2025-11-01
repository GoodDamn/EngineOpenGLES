#include <assimp/DefaultIOStream.h>
#include <assimp/Importer.hpp>
#include <assimp/postprocess.h>
#include <assimp/scene.h>
#include <android/log.h>
#include <list>
#include <jni.h>

const char* TAG = "MGObject3d.cpp";
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define NUM_TEXTURE_TYPES 3

struct MGTextures {
    aiTextureType type;
    unsigned int numTextures;
    std::string* fileNames;

    ~MGTextures() {
        delete[] fileNames;
    }
};

struct MGMesh {
    unsigned int numVertices;
    unsigned int numIndices;

    float* vertices;
    int* indices;
    MGTextures* textureTypes[
        NUM_TEXTURE_TYPES
    ];
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

MGTextures* loadTexturesTo(
    aiMaterial* material,
    aiTextureType type
) {
    unsigned int countTexture = material->GetTextureCount(
        type
    );

    if (countTexture == 0) {
        return nullptr;
    }

    auto* textures = new MGTextures;
    textures->type = type;
    textures->numTextures = countTexture;
    textures->fileNames = new std::string[
        countTexture
    ];

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

        textures[i].type = type;
        if (pos == -1 && posWin == -1) {
            textures->fileNames[i] = initStr;
            continue;
        }

        if (posWin != -1) {
            textures[i].fileNames[i] = initStr.substr(
                posWin
            );
            continue;
        }

        textures[i].fileNames[i] = initStr.substr(
            pos
        );
    }

    return textures;
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

            outMesh->textureTypes[0] = loadTexturesTo(
                material,
                aiTextureType_DIFFUSE
            );

            outMesh->textureTypes[1] = loadTexturesTo(
                material,
                aiTextureType_METALNESS
            );

            outMesh->textureTypes[2] = loadTexturesTo(
                material,
                aiTextureType_EMISSIVE
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

jobjectArray processTextures(
    JNIEnv* env,
    jclass classString,
    MGTextures* textures
) {
    if (textures == nullptr) {
        return nullptr;
    }

    jobjectArray arrFileName = env->NewObjectArray(
        textures->numTextures,
        classString,
        nullptr
    );

    std::string* fileNames = textures->fileNames;

    for (
        unsigned int textureIndex = 0;
        textureIndex < textures->numTextures;
        textureIndex++
    ) {
        LOGD("TEXTURE_JNI: %i->%s", textureIndex, fileNames[textureIndex].c_str());
        env->SetObjectArrayElement(
            arrFileName,
            textureIndex,
            env->NewStringUTF(
                fileNames[textureIndex].c_str()
            )
        );
    }

    return arrFileName;
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
        "([F[I[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;)V"
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


        jobject obj = env->NewObject(
            classElement,
            constructorElement,
            arrVert,
            arrIndices,
            processTextures(
                env,
                classString,
                mesh->textureTypes[0]
            ),
            processTextures(
                env,
                classString,
                mesh->textureTypes[1]
            ),
            processTextures(
                env,
                classString,
                mesh->textureTypes[2]
            )
        );

        for (
            unsigned char ii = 0;
            ii < NUM_TEXTURE_TYPES;
            ii++
        ) {
            if (mesh->textureTypes[ii]) {
                delete mesh->textureTypes[ii];
            }
        }

        env->SetObjectArrayElement(
            arrayObject,
            i,
            obj
        );

        delete mesh;
    }

    return arrayObject;
}
