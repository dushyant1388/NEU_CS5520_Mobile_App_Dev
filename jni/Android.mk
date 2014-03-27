LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := image_detection
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := image_detection.c
LOCAL_LDLIBS    := -llog 

include $(BUILD_SHARED_LIBRARY)