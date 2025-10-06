LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE                        := assimp
LOCAL_SRC_FILES                     := libassimp.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := engine
LOCAL_SRC_FILES := engine.cpp
LOCAL_LDLIBS := -landroid
LOCAL_SHARED_LIBRARIES := assimp
LOCAL_C_INCLUDES += $(LOCAL_PATH)
#LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog
include $(BUILD_SHARED_LIBRARY)