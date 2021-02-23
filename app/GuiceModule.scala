import bills.{BillRepo, BillRepositoryImpl}
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import tabs.{TabsRepo, TabsRepoImpl}
import tabs_users.{TabsUsersRepo, TabsUsersRepoImpl}
import users.{UsersRepo, UsersRepoImpl}

class GuiceModule(environment: Environment, configuration: Configuration)
  extends AbstractModule {

  override def configure() = {
    bind(classOf[BillRepo]).to(classOf[BillRepositoryImpl])
    bind(classOf[UsersRepo]).to(classOf[UsersRepoImpl])
    bind(classOf[TabsRepo]).to(classOf[TabsRepoImpl])
    bind(classOf[TabsUsersRepo]).to(classOf[TabsUsersRepoImpl])
  }
}