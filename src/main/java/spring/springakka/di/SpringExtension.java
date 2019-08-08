package spring.springakka.di;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 扩展组件，ApplicationContext会在SpringBoot初始化的时候加载进来 <br/>
 * 构造Props,用于生产ActorRef
 */
@Component
public class SpringExtension implements Extension {

	private ApplicationContext applicationContext;

	public void initialize(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * 该方法用来创建Props对象，依赖前面创建的SpringActorProducer,DI组件，获取到Props对象，我们就可以创建Actor bean对象
	 *
	 * @param beanName actor bean 名称
	 * @return props
	 */
	public Props props(String actorBeanName) {
		return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
	}
}