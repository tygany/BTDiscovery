package com.john.btdiscovery;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.harman.legal.LegalConfig;
import com.harman.legal.LegalManager;
import com.harman.log.CrashHandler;
import com.harman.log.DebugHelper;
import com.harman.log.Logger;
import com.harman.rating.RatingInAppMgr;
import com.harman.remote.config.RemoteConfig;
import com.harman.remote.config.RemoteConfigCallback;

import java.io.PrintWriter;
import java.io.StringWriter;

import jbl.stc.com.entity.DeviceDataModel;
import jbl.stc.com.entity.GlobalEqInfo;
import jbl.stc.com.json.parse.djpreseteq.DjPresetEqParse;
import jbl.stc.com.json.parse.preseteq.ClubEqPresetParse;
import jbl.stc.com.json.parse.preseteq.CommonPresetParse;
import jbl.stc.com.json.parse.products.ProductsParse;
import jbl.stc.com.lifecycle.ActivityLifecycleHelper;
import jbl.stc.com.manager.DeviceManager;
import jbl.stc.com.manager.LiveManager;
import jbl.stc.com.manager.ProductListManager;
import jbl.stc.com.storage.DatabaseHelper;
import jbl.stc.com.storage.PreferenceKeys;
import jbl.stc.com.storage.PreferenceUtils;
import jbl.stc.com.utils.AppUtils;
import jbl.stc.com.utils.UiUtils;
import jbl.stc.com.utils.ViewModelFactory;


public class MyApplication extends Application implements ViewModelStoreOwner {
    private static final String TAG = JBLApplication.class.getSimpleName();
    public GlobalEqInfo globalEqInfo;
    public int diameter;
    public int defaultPosition;
    private static Context context;

    public static Context getJblAppContext() {
        return context;
    }

    public static DeviceDataModel deviceDataModel;

    public static DeviceDataModel getDeviceDataModel() {
        if (deviceDataModel == null) {
            deviceDataModel = new ViewModelProvider((ViewModelStoreOwner) JBLApplication.getJblAppContext(), new ViewModelFactory()).get(DeviceDataModel.class);
        }
        return deviceDataModel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "VERSION_NAME:" + BuildConfig.VERSION_NAME);
        Logger.d(TAG, "VERSION_CODE:"+BuildConfig.VERSION_CODE);
        Logger.d(TAG, "BUILD_TYPE:"+BuildConfig.BUILD_TYPE);

        DeviceManager.getInstance(null);
        ProductListManager.getInstance();
        LiveManager.getInstance();
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
        context = this;
        PreferenceUtils.setBoolean(PreferenceKeys.ACCESS_PERMISSION_FIRST_TIME, true, this);
        if (BuildConfig.DEBUG) {
            new CrashHandler().init(this);
            DebugHelper.init(context);
            PreferenceUtils.setBoolean(PreferenceKeys.OTA_TEST_URL, true, this);
            PreferenceUtils.setBoolean(PreferenceKeys.LEGAL_TEST_URL, true, this);
            PreferenceUtils.setBoolean(PreferenceKeys.ANALYTICS_TEST_URL, true, this);
            new ANRWatchDog().setIgnoreDebugger(true).setANRListener(new ANRWatchDog.ANRListener() {
                @Override
                public void onAppNotResponding(ANRError error) {
                    StringWriter stringWriter = new StringWriter();
                    error.printStackTrace(new PrintWriter(stringWriter));
                    Logger.e(TAG, stringWriter.toString());
                }
            }).start();
        } else {
            PreferenceUtils.setBoolean(PreferenceKeys.OTA_TEST_URL, false, this);
            PreferenceUtils.setBoolean(PreferenceKeys.LEGAL_TEST_URL, false, this);
            PreferenceUtils.setBoolean(PreferenceKeys.ANALYTICS_TEST_URL, false, this);
        }
        LegalManager.INSTANCE.init(this, LegalConfig.Source.SRC_JBL_HEADPHONE);
        globalEqInfo = new GlobalEqInfo();
        ProductsParse.Companion.init(this);
        ClubEqPresetParse.init(this);
        CommonPresetParse.init(this);
        DjPresetEqParse.INSTANCE.init(this);
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        }
        initDbHelper();

        String language = PreferenceUtils.getString(PreferenceKeys.LANGUAGE_SELECTED_STRING, this, "");
        if (!TextUtils.isEmpty(language) && UiUtils.isSelectedLanguage(language)) {
            UiUtils.changeAppLanguage(language, this, false);
        } else {
            UiUtils.setDefaultLanguage(this);
        }

        AppUtils.setProductHelpTutorialClicked(this, false);

        RemoteConfig.getInstance().setCallback(new RemoteConfigCallback() {
            @Override
            public void onSuccess() {
                RatingInAppMgr.getInstance().init(getApplicationContext(),
                        null,
                        RemoteConfig.getInstance().getString(RemoteConfig.KEY_RATING_IN_APP));
            }

            @Override
            public void onFailure() { }
        });
    }

    private void initDbHelper() {
        SQLiteDatabase db = null;
        try {
            db = new DatabaseHelper(getApplicationContext()).getReadableDatabase();
        } catch (Exception e) {
            Logger.e("AnalyticsApplication", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return new ViewModelStore();
    }

}
