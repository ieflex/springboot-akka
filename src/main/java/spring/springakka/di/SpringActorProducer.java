package spring.springakka.di;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

/**
 * 实现IndirectActorProducer，用于生产Actor
 *
 * <p/>
 * Date 2019年8月8日 下午6:44:05
 * <p/>
 * @author ieflex
 */
public class SpringActorProducer implements IndirectActorProducer {

	final private ApplicationContext applicationContext;
	final private String actorBeanName;

	public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
		this.applicationContext = applicationContext;
		this.actorBeanName = actorBeanName;
	}

	@Override
	public Actor produce() {
		return (Actor) applicationContext.getBean(actorBeanName);
	}

	@Override
	public Class<? extends Actor> actorClass() {
		return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
	}
}
