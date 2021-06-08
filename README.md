# PlayFrameworkの勉強

## PlayFrameworkとは
Scala or JavaのWebアプリケーションフレームワーク  
https://www.playframework.com/

## 実行方法
1. ターミナルで下記コマンドを実行する
```
sbt run
```
2. http://localhost:9000/　に接続する

## 学んだこと
- 下記コマンドを実行すれば、雛形を作成してくれる
```
sbt new playframework/play-scala-seed.g8
```

### ディレクトリ?構成
```
app                      → Application sources
 └ assets                → Compiled asset sources
    └ stylesheets        → Typically LESS CSS sources
    └ javascripts        → Typically CoffeeScript sources
 └ controllers           → Application controllers
 └ models                → Application business layer
 └ views                 → Templates
build.sbt                → Application build script
conf                     → Configurations files and other non-compiled resources (on classpath)
 └ application.conf      → Main configuration file
 └ routes                → Routes definition
dist                     → Arbitrary files to be included in your projects distribution
public                   → Public assets
 └ stylesheets           → CSS files
 └ javascripts           → Javascript files
 └ images                → Image files
project                  → sbt configuration files
 └ build.properties      → Marker for sbt project
 └ plugins.sbt           → sbt plugins including the declaration for Play itself
lib                      → Unmanaged libraries dependencies
logs                     → Logs folder
 └ application.log       → Default log file
target                   → Generated stuff
 └ resolution-cache      → Info about dependencies
 └ scala-2.13
    └ api                → Generated API docs
    └ classes            → Compiled class files
    └ routes             → Sources generated from routes
    └ twirl              → Sources generated from templates
 └ universal             → Application packaging
 └ web                   → Compiled web assets
test                     → source folder for unit or functional tests
```
#### app配下
ソースコードが置かれるところ

#### public配下
Webサーバーから直接提供される静的アセット  
このディレクトリは、
- CSS (stylesheets ディレクトリ)
- JavaScript (javascripts ディレクトリ)
- 画像 (images ディレクトリ)

が置かれるディレクトリを用意し構成されている

#### conf配下
アプリケーションの設定ファイルを置く  
ここで重要な2つのファイルがある  
- application.conf
  - アプリケーションのメイン構成ファイル
    - 構成ファイルはのちに
- routes
  - ルーターの定義ファイル
    
### Introduction to Play
https://www.playframework.com/documentation/2.8.x/HelloWorldTutorial#Introduction-to-Play  

- 内部でAkka と　Akka HTTPを使用している
  - Akkaってなんだ
    - アクターモデル で並行処理が出来るライブラリ
      - アクターモデル
        - 複数の アクター(Actor) 同士が並行的にメッセージのやりとりを行うもの
          - アクター
            - 非同期でメッセージの送受信(スレッド、プロセス、ネットワーク間)と処理が出来て、それぞれが状態を持っているオブジェクト
            - アクター(Actor)同士は状態を気にしない。
    - https://qiita.com/kazhit/items/610bb7d0e1145ff8f423#akka%E3%81%A8%E3%81%AF
  - Akka HTTP
    - Scala/Java用のHTTPツールキット
    
### コードを読み解く
app/controller/HomeController.scala は
index.scala.html というTwirlテンプレートファイルから HTML ページを生成するメソッド
```scala
def index = Action {
  Ok(views.html.index("Your new application is ready."))
}
```
ブラウザからのリクエストを、メソッドに結びつけるには、conf/routes ファイルに設定をする
routesファイル
```
GET     /           controllers.HomeController.index
```
構成は、`HTTPメソッド・パス・メソッド` の順  

index.scala.htmlにある@main() は、main.scala.htmlにWelcomeという文字列と、HTMLを渡している  

main.scala.html  
```
@(title: String)(content: Html)
// title <- "Welcome"
// content <- index.scala.htmlの中身(?)
```


