package spring.springakka;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import spring.springakka.di.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建ActorSystem，并将其放入到spring管理，初始化ApplicationContext
 *
 * <p/>
 * Date 2019年8月8日 下午6:50:20
 * <p/>
 * 
 * @author ieflex
 */
@Configuration
class ApplicationConfiguration {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SpringExtension springExtension;

	@Bean
	public ActorSystem actorSystem() {
		ActorSystem actorSystem = ActorSystem.create("actor-system", akkaConfiguration());
		springExtension.initialize(applicationContext);
		return actorSystem;
	}

	@Bean
	public Config akkaConfiguration() {
		return ConfigFactory.load();
	}
}
