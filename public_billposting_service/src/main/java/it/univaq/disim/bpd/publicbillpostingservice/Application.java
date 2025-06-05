package it.univaq.disim.bpd.publicbillpostingservice;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableProcessApplication // Without this annotation templates and bpmn files in the resource folder won't
							// be deployed
//Add ComponentScan to include the package of your RESTController
@ComponentScan(basePackages = { "it.univaq.disim.bpd.public_billposting_service", // Your main application package
		"it.univaq.disim.bpd.example.simplerestservice1.controller", // The package where RESTController is
		"it.univaq.disim.bpd.posteravailabilityrestservice", "it.univaq.disim.bpd.publicbillpostingservice.service",
		"it.univaq.disim.bpd.publicbillpostingservice.delegate",
		"it.univaq.disim.bpd.publicbillpostingservice.config" })
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