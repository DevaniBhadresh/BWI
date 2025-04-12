# BWI Library Integration Guide

## Step 1: Add Repository to `settings.gradle`

Add the following code to your `settings.gradle` file:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url 'https://jitpack.io'
            credentials {
                username = "DevaniBhadresh"
                password = "YOUR_GITHUB_PERSONAL_ACCESS_TOKEN"
            }
        }
    }
}
```

> **Note:** Replace `YOUR_GITHUB_PERSONAL_ACCESS_TOKEN` with your actual GitHub token.

---

## Step 2: Add Plugin to `build.gradle` (Project Level)

Include the following plugin:

```gradle
id 'com.google.gms.google-services' version '4.4.2' apply false
```

---

## Step 3: Add Dependencies to `build.gradle` (App Level)

Add the Firebase plugin:

```gradle
plugins {
    id 'com.android.application'
    id 'com.google.gms.google-services'
}
```

Add the following dependencies:

```gradle
// BWI Library
implementation 'com.github.DevaniBhadresh:BWI:v1.0.0'

// Firebase Libraries
implementation platform('com.google.firebase:firebase-bom:33.8.0')
implementation 'com.google.firebase:firebase-analytics'
implementation 'com.google.firebase:firebase-config'
```

---

## Step 4: Firebase Setup

- Go to [Firebase Console](https://console.firebase.google.com/)
- Create a Firebase project
- Download the `google-services.json` file and place it inside your app’s `src/main` directory.

---

## Step 5: Initialize Firebase in `MyApplication.java`

Create `MyApplication.java` and initialize Firebase:

```java
public class MyApplication extends Application {
    public static FORemoteConfig foRemoteConfig;
    
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        foRemoteConfig = FORemoteConfig.getInstance();
    }
}
```

---

## Step 6: Implement `SplashActivity`

Create `SplashActivity.java` and extend `SupperSplashActivity`:

```java
public class SplashActivity extends SupperSplashActivity {
    private final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.foRemoteConfig.setInterfaz(this);
    }

    @Override
    public void onRemoteFetch(boolean isFetched) {
        super.onRemoteFetch(isFetched);
        Log.d(TAG, "Remote config fetch successful");
    }

    @Override
    protected Map<String, AdConfig> getAdConfigurations() {
        return null;
    }

    @Override
    protected SplashFragment getSplashFragment() {
        return null;
    }

    @Override
    protected Fragment getLanguageFragmentClass() {
        return null;
    }

    @Override
    protected Fragment getIntroFragmentClass() {
        return null;
    }

    @Override
    protected int getActivityLayoutId() {
        return 0;
    }

    @Override
    public void completeOnboarding() {
    }
}
```

---

## Step 7: Configure Ads

```java
@Override
protected Map<String, AdConfig> getAdConfigurations() {
    Map<String, AdConfig> adConfigs = new HashMap<>();
    if (FORemoteConfig.getInstance().isAdsEnabled()) {
        adConfigs.put(AdKeys.SPLASH_NATIVE_AD, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().getSplashNativeAd()));
    }
    return adConfigs;
}
```

---

## Step 8: Implement `SplashFragment`

```java
@Override
protected SplashFragment getSplashFragment() {
    return SplashFragment.newInstance(
        R.layout.fragment_splash,
        R.id.ad_container,
        R.id.shimmer_container_native,
        getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutSplashNativeAd())
    );
}
```

---

## Step 9: Implement `LanguageFragment`

```java
@Override
protected Fragment getLanguageFragmentClass() {
    return LanguageSelectionFragment.newInstance(
        R.layout.fragment_language_selection,
        R.id.ad_container,
        getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutLanguageNativeAd1()),
        getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutLanguageNativeAd2()),
        R.id.btn_done,
        R.id.recycler_view_languages,
        R.layout.item_language,
        getLanguages()
    );
}
```

---

## Step 10: Implement `IntroFragment`

```java
@Override
protected Fragment getIntroFragmentClass() {
    return IntroductionSliderFragment.newInstance(
        R.layout.fragment_introduction_slider,
        R.id.ad_container,
        getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutIntroNativeAd()),
        R.id.introduction_slider,
        R.layout.slide_1,
        R.layout.slide_2,
        R.layout.slide_3
    );
}
```

- Sample code of fragment_introduction_slider.xml
- Please follow step : 14 for layout_introduction_slider.xml for app:customLayout attributes

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:background="@color/colorPrimaryBackground"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <com.bwi.onboard.slider.IntroductionSlider
        android:id="@+id/introductionSlider"
        app:layout_constraintTop_toTopOf="parent"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:customLayout="@layout/layout_introduction_slider" />

    <!-- Ad container -->
    <FrameLayout
        android:id="@+id/ad_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>

```

---

## Step 11: Set Up `completeOnboarding()`

```java
@Override
public void completeOnboarding() {
    startActivity(new Intent(this, DashboardActivity.class));
    finish();
}
```

---

## Step 12: Prepare Language List

```java
private List<Language> getLanguages() {
    List<Language> languages = new ArrayList<>();
    languages.add(new Language(R.drawable.flag_us_en, "English", "en"));
    languages.add(new Language(R.drawable.flag_france_fr, "French", "fr"));
    languages.add(new Language(R.drawable.flag_spain_sp, "Spanish", "es"));
    languages.add(new Language(R.drawable.flag_india_hi, "Hindi", "hi"));
    return languages;
}
```

---

## Step 13: Detect Device Theme

```java
private String getDefaultDeviceTheme() {
    int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
    return (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) ? "dark" : "light";
}
```
## Step 14: Follow guideline to Design IntroductionSlider (layout_introduction_slider.xml) layout

- Make sure to use following UI component with specified IDs
- ViewPager2 like
```xml
<androidx.viewpager2.widget.ViewPager2
    android:id="@+id/view_pager"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
- TabLayout for indication
```xml
<com.google.android.material.tabs.TabLayout
            android:id="@+id/tab_layout"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:tabIndicatorColor="@color/colorPrimary"
            app:tabSelectedTextColor="@color/black"
            app:tabIndicatorHeight="4dp" />
```
- Next Button
```xml
<Button     
    android:id="@+id/btnNext"
    android:text="Next"
    android:background="@drawable/next_bg"
    android:layout_width="wrap_content"
    android:layout_height="@dimen/_32sdp"/>
```
---

## Final Notes
- Ensure you have the correct Firebase setup.
- Replace placeholders with actual values where necessary.
- Keep your GitHub token private and secure.

🚀 **Your BWI Library is now ready to use!**

