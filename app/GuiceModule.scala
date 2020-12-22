import bills.{BillRepo, BillRepository}
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}

class GuiceModule(environment: Environment, configuration: Configuration)
  extends AbstractModule {

  override def configure() = {
    bind(classOf[BillRepo]).to(classOf[BillRepository])
  }
}