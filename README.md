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

# DI 依存性注入

## Daggerのみ
---


<br>

## Hilt
---


<br>
