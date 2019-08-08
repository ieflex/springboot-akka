# springboot-akka
Springboot and Akka integration - basics

# Dependencies
    <dependency>
			<groupId>com.typesafe.akka</groupId>
			<artifactId>akka-actor_2.11</artifactId>
			<version>2.4.3</version>
		</dependency>

		<dependency>
			<groupId>com.typesafe.akka</groupId>
			<artifactId>akka-slf4j_2.11</artifactId>
			<version>2.4.3</version>
		</dependency>
    
# Configuring the Akka system

```@Configuration
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
```

# creating worker 

```@Component("workerActor")
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
}
```

# kick start the system


```try {
            ActorRef workerActor = actorSystem.actorOf(springExtension.props("workerActor"), "worker-actor");

            workerActor.tell(new WorkerActor.Request(), null);
            workerActor.tell(new WorkerActor.Request(), null);
            workerActor.tell(new WorkerActor.Request(), null);

            FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
            Future<Object> awaitable = Patterns.ask(workerActor, new WorkerActor.Response(), Timeout.durationToTimeout(duration));

            logger.info("Response: " + Await.result(awaitable, duration));
        } finally {
            actorSystem.terminate();
            Await.result(actorSystem.whenTerminated(), Duration.Inf());
        }
 ```
        
 # Our business service does only to print the worker threaad and a counter
 And running the application -> 
 
```12/25 08:59:11.009 INFO [actor-system-akka.actor.default-dispatcher-8] i.k.s.s.s.BusinessService - Perform: spring.springakka.actor.WorkerActor@10f77647 1
12/25 08:59:11.009 INFO [actor-system-akka.actor.default-dispatcher-8] i.k.s.s.s.BusinessService - Perform: spring.springakka.actor.WorkerActor@10f77647 2
12/25 08:59:11.009 INFO [actor-system-akka.actor.default-dispatcher-8] i.k.s.s.s.BusinessService - Perform: spring.springakka.actor.WorkerActor@10f77647 3
```
