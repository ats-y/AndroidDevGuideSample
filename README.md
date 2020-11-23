# DevGuideSamples

Android Developersガイド(https://developer.android.com/guide?hl=ja)に載っているAndroidアプリを作成する基本技術サンプル。

<br>

## ログ出力
---
Timberとlogback-androidを使用。

* Timber

    https://github.com/JakeWharton/timber

* logback-android

    https://tony19.github.io/logback-android/


ログ出力処理をTimberでラッピングし、出力実体はlogback-androidで実装している。

DevGuideSampleApplication.onCreate()でTimberを初期化している。出力実体をlogback-androidとしたTreeにしている。

ログの出力先・方法を変更したい場合はそれ用のTreeを作成する。

logback-androidの出力設定は`app/src/main/assets/logback.xml`で定義する。設定項目は[logback-android wiki](https://github.com/tony19/logback-android/wiki)および[logbackマニュアル](http://logback.qos.ch/manual/index_ja.html)参照。

<br>

## DI 依存性注入
---

以下の関連となっているデータクラスをMainActivityにインジェクションする。

    WeatherRepository -> OpenWeather

### Daggerのみ
---
build.gradle(app)

```gradle
// Dagger
def dagger_version = "2.30"
implementation "com.google.dagger:dagger:$dagger_version"
annotationProcessor "com.google.dagger:dagger-compiler:$dagger_version"
```

WeatherRepository.java
```java
public class WeatherRepository {

    private OpenWeather mOpenWeather;

    // @InjectでDaggerにこのクラスを生成する方法を伝える。
    // 依存注入の際にこのコンストラクタでオブジェクトが生成される。
    @Inject
    public WeatherRepository(OpenWeather api){
        mOpenWeather = api;
    }
}
```

OpenWeather.java
```java
public class OpenWeather {

    // @InjectでDaggerにこのクラスを生成する方法を伝える。
    // 依存注入の際にこのコンストラクタでオブジェクトが生成される。
    @Inject
    public OpenWeather(){
    }
}
```

IApplicationComponent.java
```java
@Singleton
@Component
public interface IApplicationComponent {

    /**
     * DaggerにMainActivityが依存注入を要求していること伝える。
     * @param mainActivity
     */
    void inject(MainActivity mainActivity);
}
```

Application.java
```java
public class DevGuideSampleApplication extends Application {

    // DaggerIApplicationComponentは、IApplicationComponent作成直後にビルドすると自動生成される。
    public IApplicationComponent appComponent = DaggerIApplicationComponent.create();
}
```

MainActivity.java
```java
public class MainActivity extends AppCompatActivity {

    // @Injectでフィールドインジェクションの対象であることを伝える。
    @Inject
    public WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // フィールドインジェクションする。
        // このタイミングで@Injectフィールドにインジェクションされる。
        // mWeatherRepositoryにインスタンスが格納され、
        // さらにWeatherRepository.mOpenWeatherにOpenWeatherのインスタンスが生成される。
        ((DevGuideSampleApplication)getApplicationContext()).appComponent.inject(this);
    }
}
```

<br>

### Hiltの場合
---

build.gradle(module)
```gradle
buildscript {
    dependencies {
        classpath 'com.google.dagger:hilt-android-gradle-plugin:2.28-alpha'
    }
}
```

build.gradle(app)
```gradle
apply plugin: 'dagger.hilt.android.plugin'

dependencies {
    // Dagger
    def dagger_version = "2.30"
    implementation "com.google.dagger:dagger:$dagger_version"
    annotationProcessor "com.google.dagger:dagger-compiler:$dagger_version"

    // Dagger Android
    api "com.google.dagger:dagger-android:$dagger_version"
    api "com.google.dagger:dagger-android-support:$dagger_version"
    annotationProcessor 'com.google.dagger:dagger-android-processor:2.27'

    // Dagger Hilt
    implementation "com.google.dagger:hilt-android:2.28-alpha"
    annotationProcessor "com.google.dagger:hilt-android-compiler:2.28-alpha"
}
```

Application.java
```java
// @HiltAndroidAppでこのアプリはHiltを使うことを宣言。
@HiltAndroidApp
public class DevGuideSampleApplication extends Application {
}
```

MainActivity.java
```java
// @AndroidEntryPointで、このActivityにインジェクション対象があることを宣言。
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    // @Injectでフィールドインジェクションの対象であることを伝える。
    @Inject
    public WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hiltだと↓がなくてもこの時点でインジェクションされている。
        // ((DevGuideSampleApplication)getApplicationContext()).appComponent.inject(this);
    }
}
```

IApplicationComponent.javaは不要。

<br>

## MVVM
---

|種類|Androidソース|
|---|---|
|View|レイアウトXML、Activity・Fragmentなどの画面クラス。|
|ViewModel|画面の業務ロジックを抽出したクラス。|
|Model|上記以外のクラス。|

<br>

### ViewModel
---
MVVMとしては、画面描画(View)と画面の業務ロジック(ViewModel)を分離し、業務ロジック・データを抽出できるという特徴がある。

Androidの場合、画面回転などでActivityクラスのインスタンスが再生成されてEditTextの入力内容が空になるが、ViewModelで業務データを保持して表示内容をViewとバインディングさせることでActivityインスタンスが再生成されても表示を維持できるという利点がある。

Gradle設定は以下を参照。<br>
https://developer.android.com/topic/libraries/architecture/viewmodel?hl=ja

Viewを「ListTrialFragment」、これに対応するViewModelを「ListTrialViewModel」として作成する。

#### ViewModelクラスを作成する
---
androidx.lifecycle.ViewModelのViewModelを継承してViewModelクラスを作成する。

ListTrialViewModel.java
```java
public class ListTrialViewModel extends ViewModel {
}
```

#### ViewでViewModelを生成する
---
ListTrialFragment.java
```java
public class ListTrialFragment extends Fragment {

    private ListTrialViewModel mViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ViewModelを生成する。
        // 生成済みの場合は、生成済みのViewModelインスタンスが返る。
        mViewModel = new ViewModelProvider(this).get(ListTrialViewModel.class);
    }
}
```

#### レイアウトXMLとViewModelをビューバインディングでバインディングする
---
build.gradle(app)
```gradle
android {
    viewBinding {
       enabled = true
    }
}
```

レイアウトXMLのルート要素を「Layout」にして、ViewModelとバインディングする変数を定義する。

```XML
<?xml version="1.0" encoding="utf-8"?>
<layout>
    <data>
        <!-- ViewModelとバインディングする変数定義 -->
        <variable
            name="viewModel"
            type="com.atsy.devguidesample.viewmodels.ListTrialViewModel" />
    </data>

    <!--レイアウト-->
    <androidx.constraintlayout.widget.ConstraintLayout>
        <!--略-->
    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

ListTrialFragment.java
```java
public class ListTrialFragment extends Fragment {

    private ListTrialViewModel mViewModel;

    // レイアウトXML「ListTrialFragment.xml」のビューバインディングクラスが
    // 「{レイアウトXMLファイル名}Binding」という名前で自動生成される。
    private ListTrialFragmentBinding mViewBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // ビューバインディングクラスからViewを生成する。
        mViewBinding = ListTrialFragmentBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ViewModelを生成する。
        mViewModel = new ViewModelProvider(this).get(ListTrialViewModel.class);

        // レイアウトXML「ListTrialFragment.xml」の変数「viewModel」に
        // ViewModelインスタンスを代入（バインディグ）する。
        // ※setViewModel()は、「ListTrialFragment.xml」に変数「viewModel」を定義すると
        // 自動的に生成される。
        mViewBinding.setViewModel(mViewModel);
    }
```
<br>

#### レイアウトXMLの表示プロパティとViewModelのメンバ変数をバインディングする
---

https://developer.android.com/topic/libraries/data-binding?hl=ja

EditBoxのtextプロパティとViewModelのメンバ変数を双方向バインディングする。

[ビューバインディング](#レイアウトXMLとViewModelをビューバインディングでバインディングする)の`mViewBinding.setViewModel(mViewModel);`でレイアウトXMLのviewModel変数とViewModelをバインディングさせたので、さらにレイアウトのviewModelのプロパティとViewModelのメンバ変数をバインディングさせる。

list_trial_fragment.xml
```XML
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>
        <variable
            name="viewModel"
            type="com.atsy.devguidesample.viewmodels.ListTrialViewModel" />
    </data>

    <!-- 略 -->

    <!-- textプロパティをviewModel.inputTextとバインディング -->
    <EditText
        android:id="@+id/inputEdit"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@={viewModel.inputText}"/>

    <!-- 略 -->
</layout>
```

ListTrialViewModel.java
```JAVA
public class ListTrialViewModel extends ViewModel {

    // 監視可能なオブジェクトで定義。
    // EditBoxのtextを監視。
    public ObservableField<String> inputText = new ObservableField<>();
}
```
これで、EditTextに入力した値が自動的にListTrialViewModel.inputTextに反映される。

さらに画面が回転してもEditBoxの入力内容は維持される。

<br>

### ViewModelを引数付きで生成する
---

[ViewでViewModelを生成する](#ViewでViewModelを生成する)でのViewModel生成では初回のインスタンス生成時、ViewModelの引数なしコンストラクタが呼ばれる。

DI導入などで引数付きでViewModelのインスタンスを生成したい場合は`ViewModelProvider.Factory`を利用する。

ListTrialViewModel.java
```JAVA
public class ListTrialViewModel extends ViewModel {

    /* 略 */

    /** ListTrialViewModelのファクトリ */
    static public class Factory implements ViewModelProvider.Factory{

        private WeatherRepository mWeatherReopsitory;

        public Factory(WeatherRepository weatherRepository){
            mWeatherReopsitory = weatherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ListTrialViewModel(mWeatherReopsitory);
        }
    }
}
```

ListTrialFragment.java
```JAVA
public class ListTrialFragment extends Fragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // ViewModelを引数ありで生成するためにListTrialViewModel.Factoryを利用。
        //mViewModel = new ViewModelProvider(this).get(ListTrialViewModel.class);
        ListTrialViewModel.Factory viewModelFactory = new ListTrialViewModel.Factory(mWeatherRepository);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(ListTrialViewModel.class);
    }
}
```

<br>

## Retrofit
---

HTTP操作（GET/POST/PUT/DELETE）をJavaのInterfaceで呼び出せるようにしたHTTPクライアント。

https://square.github.io/retrofit/

HTTPクライアントは[OkHttp3](https://github.com/square/okhttp)。

OkHttp3の[Logging Interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)でHTTPのログを出力することができる。

<br>

## 非同期
---

