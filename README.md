# Play Hello World Web Tutorial for Scala

To follow the steps in this tutorial, you will need the correct version of Java and sbt. The template requires:

* Java Software Developer's Kit (SE) 1.8 or higher
* sbt 1.3.4 or higher. Note: if you downloaded this project as a zip file from <https://developer.lightbend.com>, the file includes an sbt distribution for your convenience.

To check your Java version, enter the following in a command window:

```bash
java -version
```

To check your sbt version, enter the following in a command window:

```bash
sbt sbtVersion
```

If you do not have the required versions, follow these links to obtain them:

* [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [sbt](http://www.scala-sbt.org/download.html)

## Build and run the project

This example Play project was created from a seed template. It includes all Play components and an Akka HTTP server. The project is also configured with filters for Cross-Site Request Forgery (CSRF) protection and security headers.

To build and run the project:

1. Use a command window to change into the example project directory, for example: `cd play-scala-hello-world-web`

2. Build the project. Enter: `sbt run`. The project builds and starts the embedded HTTP server. Since this downloads libraries and dependencies, the amount of time required depends partly on your connection's speed.

3. After the message `Server started, ...` displays, enter the following URL in a browser: <http://localhost:9000>

The Play application responds: `Welcome to the Hello World Tutorial!`

---
↓ ここからメモ

---
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

## HelloWorldの実装
https://www.playframework.com/documentation/2.8.x/ImplementingHelloWorld

主な手順
1. HelloWorldページを作成
2. アクションメソッドを追加
3. ルートの定義
4. 挨拶をカスタマイズする

①  
app/views に、`hello.scala.html`を作成する。  
ファイルの中身
```scala
@()(implicit assetsFinder: AssetsFinder)

@main("Hello") {
    <section id="top">
        <div class="wrapper">
            <h1>Hello World</h1>
        </div>
    </section>
}
```
このTwirlとHTMLが果たすこと
- @は、テンプレートエンジンに指示を出す
- @main("Hello")は、main.scala.htmlを呼び出して、Helloという文字列をページタイトルとして、渡している
- content引数には、HelloWorldの{} の中が渡されている
  - mainテンプレートはこれを使って、ページに挿入する
- 今回実行するときは、assetsFinderはコメントアウトした方が良いかもしれない
    
②  
新しいアクションメソッドを追加するには、  
今回は、app/controllers/HomeController.scalaに追加する  
そして、下記を追加する  
```scala
def hello = Action {
  Ok(views.html.hello())
}
```

③  
このままだと、ブラウザがhelloページをリクエストしても何も起きないので、ルートを設定してあげる必要がある  
新しいルートを定義するには、conf/routes ファイルに追加する  
```text
GET     /hello      controllers.HomeController.hello
```

routeファイルに追加すると、Playのrouteコンパイラは、コントローラーのインスタンスを使用して、  
そのアクションを呼び出すrouteクラスを自動生成する  
詳しくは、[こちら](https://www.playframework.com/documentation/2.8.x/ScalaRouting#HTTP-routing)
デフォルトでは、コントローラーインスタンスは、DIで作成されている  
これも詳しくは、[こちら](https://www.playframework.com/documentation/2.8.x/ScalaDependencyInjection)

④
HTTPリクエストパラメータを受け取れるようにする  

app/controllers/HomeController.scala を書き換える  
```scala
def hello(name: String) = Action {
  Ok(views.html.hello())
}
```

routes ファイルも書き換える  
```text
GET  /hello        controllers.HomeController.hello(name: String)
```

Twirlテンプレートでは、全ての変数とそのタイプを宣言する必要がある  
- ファイルの先頭に下記を追加する
```scala
@(name: String)
```
- 上記の変数を使用するために下記のように変更する
```html
<h1>Hello @name!</h1>
```

- Playは、戻り値のrenderメソッドに型付きパラメータが必要であることを通知するコンパイルエラーを出してくれる
  - 修正するには、HomeControllerのhelloメソッドを変更してビューのレンダリング時にnameパラメータを渡してあげないといけない
    
## サンプル
https://www.playframework.com/documentation/2.8.x/Tutorials

色々なサンプルがあるようだ

## Scalaのドキュメント
https://www.playframework.com/documentation/2.8.x/ScalaHome

## 構成API
Playは、Typesafeの構成ライブラリを使う  
Configurationと呼ばれる優れたScalaのラッパーも提供する  

### Typesafeの構成ファイル詳細
https://www.playframework.com/documentation/2.8.x/ConfigFile

## 構成へのアクセス
通常は、DIを介してConfiguration オブジェクトを取得する  
もしくは、単にConfiguration インスタンスをコンポーネントに渡すことで取得する

```scala
class MyController @Inject() (config: Configuration, c: ControllerComponents) extends AbstractController(c) {
  def getFoo = Action {
    Ok(config.get[String]("foo"))
  }
}
```
getメソッドは、最もよく使う。
↑は、設定ファイルのパスにある単一の値を取得するために使用する  

コメントアウトしてる部分をapplication.confに定義して実行すると、画面に表示される
```scala
// foo = bar
config.get[String]("foo")

// bar = 8
config.get[Int]("bar")

// baz = true
config.get[Boolean]("baz")

// listOfFoos = ["bar", "baz"]
config.get[Seq[String]]("listOfFoos")
```

Configurationでは、有効な値のセットに対する検証もサポートしている
```scala
config.getAndValidate[String]("foo", Set("bar", "baz"))
```
application.confに、bar もしくは bazがないと、`Incorrect value, one of (bar, baz) was expected.`となる
```scala
"foo" = "baz"
// or
"foo" = "bar"
```

### ConfigLoader
独自のConfigLoaderを定義することで構成をカスタムタイプに変換できる
```scala
case class AppConfig(title: String, baseUri: URI)

object AppConfig {
  implicit val configLoader: ConfigLoader[AppConfig] = new ConfigLoader[AppConfig] {
    def load(rootConfig: Config, path: String): AppConfig = {
      val config = rootConfig.getConfig(path)
      AppConfig(
        title = config.getString("title"),
        baseUri = new URI(config.getString("baseUri"))
      )
    }
  }
}
```
```scala
// app.config = {
//   title = "My App
//   baseUri = "https://example.com/"
// }
config.get[AppConfig]("app.config")
```
このようにして取得できる

### OptionLoader
getOptional[A]メソッドによるOption設定キーの取得がサポートされている

キーが存在しない場合は、Noneを返す  
このメソッドを使用する代わりに設定ファイルでオプションキーをnullに設定して、get[Option[A]]を使用するのがおすすめ(らしい)

## HTTP Programing
https://www.playframework.com/documentation/2.8.x/ScalaActions

### Actionとは？
Playは、受信するリクエストのほとんどはアクションによって処理される
`play.api.mvc.Action`は基本的に、`Request => Result`関数である   
要求を処理し、クライアントに送信される結果を生成する  

```scala
  def echo: Action[AnyContent] = Action { request =>
  Ok("Got request [" + request + "]")
}
```
アクションは、Webクライアントに送信するHTTP応答を表すplay.api.mvc.Result値を返す  
上記の例では、  
`Ok` は、text/plainレスポンスボディを含む `200 OK`レスポンスを構築する

### Actionの構築
BaseControllerを継承するController内では、Actionの値がデフォルトのアクションビルダーになる
(AbstractControllerはBaseControllerを継承している)  

アクションビルダーにはアクションを作成するためのいくつかのヘルパーが含まれている  

- 引数として結果を返す式ブロックを取る
  - これは受信したリクエストへのを参照は取得しない
  - このActionを呼び出したHTTPリクエストにアクセスすると便利な場合がある(?)
```scala
Action {
  Ok("Hello world")
}
```

- 引数として、関数`Request => Result`を受け取る別のアクションビルダーがある
```scala
Action { request =>
  Ok("Got request [" + request + "]")
}
```

- リクエストパラメータを暗黙的にしておくと、それを必要とする他のAPIで暗黙的に使用することができるので便利
```scala
Action { implicit request =>
  Ok("Got request [" + request + "]")
}
```

- コードをメソッドに分割した場合、アクションからの暗黙的な要求を渡すことができる
```scala
def action = Action { implicit request =>
  anotherMethod("Some para value")
  Ok("Got request [" + request + "]")
}

def anotherMethod(p: String)(implicit request: Request[_]): Unit = {
  // リクエストへのアクセスが必要なことを行う
}
```

- 追加のBodyParser引数を指定すること
  - BodyParserは後半で
```scala
Action(parse.json) { implicit request =>
  Ok("Got request [" + request + "]")
}
```
Any content body parser

### コントローラーはアクションジェネレーター
Playのコントローラーはアクションの値を生成するオブジェクトに過ぎない  
コントローラーはDIを利用するためのクラスとして定義される

※ コントローラーはクラスで定義されるのが推奨されている

最も簡単なユースケース: アクション値を返すパラメーターのないメソッド  
```scala
package controllers

  import javax.inject.Inject

  import play.api.mvc._

  class Application @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
    def index = Action {
      Ok("It works!")
    }
  }
```
アクションジェネレーターメソッドには、パラメーターを含めることができ、これらのパラメーターはアクションクロージャーによって取り込むことができる
```scala
def hello(name: String) = Action {
  Ok("Hello " + name)
}
```

### 簡単なResults
ステータスコード、HTTPヘッダー、ボディを含むHTTP結果をWebクライアントに送信することができる  
それらの結果は、play.api.mvc.Resultによって定義される  

```scala
import play.api.http.HttpEntity

def index = Action {
  Result(
    header = ResponseHeader(200, Map.empty),
    body = HttpEntity.Strict(ByteString("Hello world!"), Some("text/plain"))
  )
}
```
先ほどまで利用していた、Okのような一般的な結果を出すためのヘルパーも用意されている
```scala
def index = Action {
  Ok("Hello world!")
}
```

他にもある
```scala
val ok           = Ok("Hello world!")
val notFound     = NotFound
val pageNotFound = NotFound(<h1>Page not found</h1>)
val badRequest   = BadRequest(views.html.form(formWithErrors))
val oops         = InternalServerError("Oops")
val anyStatus    = Status(488)("Strange response type")
```
これらのヘルパーはすべて、play.api.mvc.Resultsトレイトとコンパニオンオブジェクトにある

### リダイレクト
ブラウザを新しいURLにリダイレクトするのも、シンプルな結果の一種
ただし、これらの結果タイプはレスポンスボディを受け取れない  

リダイレクトを作成するために利用できるヘルパー
```scala
def index = Action {
  Redirect("/user/home")
}
```
デフォルトでは、`303 SEE_OTHER` を使うが必要に応じて具体的なコードを設定することもできる
```scala
def index = Action {
  Redirect("/user/home", MOVED_PERMANENTLY)
}
```

### ダミーページ
TODOを使うと、まだ実装されていませんというページになる
```scala
def index(name: String) = TODO
```