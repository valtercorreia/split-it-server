import bills.{BillRepo, BillRepositoryImpl}
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import users.{UsersRepo, UsersRepoImpl}

class GuiceModule(environment: Environment, configuration: Configuration)
  extends AbstractModule {

  override def configure() = {
    bind(classOf[BillRepo]).to(classOf[BillRepositoryImpl])
    bind(classOf[UsersRepo]).to(classOf[UsersRepoImpl])
  }
}