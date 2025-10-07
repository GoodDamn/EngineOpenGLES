LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE                        := assimp
LOCAL_SRC_FILES                     := libassimp.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := MGObject3d
LOCAL_SRC_FILES := MGObject3d.cpp
LOCAL_LDLIBS := -llog -landroid
LOCAL_SHARED_LIBRARIES := assimp
LOCAL_C_INCLUDES += $(LOCAL_PATH)
include $(BUILD_SHARED_LIBRARY)