#include <assimp/DefaultIOStream.h>
#include <assimp/Importer.hpp>
#include <assimp/postprocess.h>
#include <assimp/scene.h>
#include <android/log.h>
#include <jni.h>

const char* TAG = "MGObject3d.cpp";
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)

class MGStream: public Assimp::DefaultIOStream {
public:
    MGStream(
        FILE* pFile,
        const std::string& filePath
    ): Assimp::DefaultIOStream(
        pFile,
        filePath
    ) {}
};

std::string jStringToStd(
    JNIEnv* env,
    jstring inp
) {

    const jclass classString = env->GetObjectClass(
        inp
    );

    const jmethodID methodGetBytes = env->GetMethodID(
        classString,
        "getBytes", "(Ljava/lang/String;)[B"
    );

    const jbyteArray jbytesString = static_cast<
        jbyteArray
    >(
        env->CallObjectMethod(
        inp,
        methodGetBytes,
            env->NewStringUTF("UTF-8")
        )
    );

    size_t length = static_cast<size_t>(
        env->GetArrayLength(jbytesString)
    );

    jbyte* pointerArray = env->GetByteArrayElements(
        jbytesString,
        nullptr
    );

    std::string output = std::string(
        reinterpret_cast<char*>(pointerArray),
        length
    );

    env->ReleaseByteArrayElements(
        jbytesString,
        pointerArray,
        JNI_ABORT
    );

    env->DeleteLocalRef(
        jbytesString
    );

    env->DeleteLocalRef(
        classString
    );

    return output;
}

extern "C"
JNIEXPORT void JNICALL
Java_good_damn_engine_opengl_MGObject3d_createFromStream(
    JNIEnv *env,
    jclass clazz,
    jstring path
) {
    const char* jPath = jStringToStd(
        env,
        path
    ).c_str();

    LOGD("%s", jPath);

    FILE* file = ::fopen(
        jPath,
        "r"
    );

    if (!file) {
        LOGD("file do not exists");
        return;
    }

    Assimp::Importer* importer = new Assimp::Importer();

    LOGD("set IO handler");

    const aiScene* scene = importer->ReadFile(
        jPath,
        aiProcess_Triangulate |
        aiProcess_GenSmoothNormals |
        aiProcess_FlipUVs |
        aiProcess_JoinIdenticalVertices
    );

    if (!scene) {
        LOGD("%s", importer->GetErrorString());
        delete importer;
        return;
    }

    LOGD("scene loaded");
    delete importer;
}
