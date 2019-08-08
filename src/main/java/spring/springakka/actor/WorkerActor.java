package spring.springakka.actor;

import akka.actor.UntypedActor;

import spring.springakka.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 一般情况下我们的Actor都需要继承自UntypedActor，并实现其onReceive方法。onReceive用于接收消息，你可以在其中实现对消息的匹配并做不同的处理。
 * 
 */
@Component("workerActor")
@Scope("prototype")
public class WorkerActor extends UntypedActor {

	@Autowired
	private BusinessService businessService;

	private int count = 0;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Request) {
			businessService.perform(this + " " + (++count));
		} else if (message instanceof Response) {
			getSender().tell(count, getSelf());
		} else {
			unhandled(message);
		}
	}

	public static class Request {
	}

	public static class Response {
	}
}
