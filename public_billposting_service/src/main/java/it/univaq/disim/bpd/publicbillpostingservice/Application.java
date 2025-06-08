package it.univaq.disim.bpd.publicbillpostingservice;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableProcessApplication // Without this annotation templates and bpmn files in the resource folder won't
							// be deployed
@ComponentScan(basePackages = { "it.univaq.disim.bpd.publicbillpostingservice.config",
		"it.univaq.disim.bpd.publicbillpostingservice.delegate",
		"it.univaq.disim.bpd.publicbillpostingservice.endpoint",
		"it.univaq.disim.bpd.publicbillpostingservice.exception",
		"it.univaq.disim.bpd.publicbillpostingservice.service" })
public class Application {

//	@Autowired
//	private RuntimeService runtimeService;

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Don’t Auto-Start the Process on Deployment This way, the process is started
	 * only when the external message (poster-availability-request) is sent via API
	 * — and that API provides the necessary variables like
	 * posterAvailabilityRequest.
	 */
//	@EventListener
//	public void processPostDeploy(PostDeployEvent event) {
//		runtimeService.startProcessInstanceByKey("PublicBillpostingService");
//	}

}