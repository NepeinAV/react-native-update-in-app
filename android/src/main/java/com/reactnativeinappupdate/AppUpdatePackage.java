package com.reactnativeinappupdate;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.TurboReactPackage;
import com.facebook.react.uimanager.ViewManager;

import java.util.HashMap;
import java.util.Map;

public class AppUpdatePackage extends TurboReactPackage {
    @Nullable
    @Override
    public NativeModule getModule(String name, ReactApplicationContext reactContext) {
        if (name.equals(AppUpdateModule.NAME)) {
            return new AppUpdateModule(reactContext);
        } else {
            return null;
        }
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return () -> {
            final Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();

            boolean isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;

            moduleInfos.put(
                AppUpdateModule.NAME,
                new ReactModuleInfo(
                    AppUpdateModule.NAME,
                    AppUpdateModule.NAME,
                    false,
                    false,
                    true,
                    false,
                    isTurboModule
                )
            );

            return moduleInfos;
        };
    }

}
