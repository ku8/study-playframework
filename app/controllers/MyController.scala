package controllers

import play.api.Configuration
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject

class MyController @Inject() (config: Configuration, c: ControllerComponents) extends AbstractController(c) {

  def getFoo: Action[AnyContent] = Action {
//    Ok(config.get[String]("foo"))
    Ok(config.getAndValidate[String]("foo", Set("bar", "baz")))
  }

  def index(name: String): Action[AnyContent] = TODO

}
