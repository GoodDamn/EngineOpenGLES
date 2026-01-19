LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE                        := assimp
LOCAL_SRC_FILES                     := assimp/bin/$(TARGET_ARCH_ABI)/libassimp.so
LOCAL_EXPORT_C_INCLUDES				:= assimp/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := engine
LOCAL_SRC_FILES := ASObject3d.cpp
LOCAL_LDLIBS := -llog -landroid
LOCAL_SHARED_LIBRARIES := assimp
#LOCAL_C_INCLUDES += $(LOCAL_PATH)
include $(BUILD_SHARED_LIBRARY)