package it.univaq.disim.bpd.publicbillpostingservice.delegate;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.univaq.disim.bpd.posteravailabilityrestservice.PosterAvailabilityRequest;
import it.univaq.disim.bpd.publicbillpostingservice.model.Bill;
import it.univaq.disim.bpd.publicbillpostingservice.model.CheckAvailabilityResponse;
import it.univaq.disim.bpd.publicbillpostingservice.model.User;
import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@Component("requestFileWriterDelegate")
public class RequestFileWriter implements JavaDelegate {
	@Autowired
	private Configuration freemarkerConfig;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// Get process variables
		User userData = (User) execution.getVariable("userData");
		PosterAvailabilityRequest posterAvailabilityRequest = (PosterAvailabilityRequest) execution.getVariable("posterAvailabilityRequest");
		Map<String, List<Zone>> selectedZones = (Map<String, List<Zone>>) execution.getVariable("selectedZones");
		CheckAvailabilityResponse checkAvailabilityResponse = (CheckAvailabilityResponse) execution.getVariable("checkAvailabilityResponse");
		Bill orderInformation = (Bill) execution.getVariable("orderInformation");

		// Prepare template model
		Map<String, Object> model = new HashMap<>();
		model.put("userData", userData);
		model.put("posterAvailabilityRequest", posterAvailabilityRequest);
		model.put("selectedZones", selectedZones);
		model.put("checkAvailabilityResponse", checkAvailabilityResponse);
		model.put("orderInformation", orderInformation);

		// Load template
		Template template = freemarkerConfig.getTemplate("requestFile.ftl");

		// Determine if running from a JAR
		boolean isRunningFromJar = RequestFileWriter.class.getProtectionDomain().getCodeSource().getLocation().getPath()
				.endsWith(".jar");

		// Set base path accordingly
		String basePath;
		if (isRunningFromJar) {
			// Running from JAR: use the directory where the JAR is located
			basePath = Paths.get(RequestFileWriter.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getParent().toString();
		} else {
			// Dev mode: go up one directory to project root
			basePath = Paths.get(System.getProperty("user.dir")).getParent().toString();
		}

		// Create output/ directory if it doesn't exist
		File outputDir = new File(basePath, "output");
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		// Generate filename with timestamp
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String filename = "bill_" + timestamp + ".txt";
		File outputFile = new File(outputDir, filename);

		// Process FreeMarker template into the file
		try (Writer out = new FileWriter(outputFile)) {
			template.process(model, out);
			System.out.println("✅ File written to: " + outputFile.getAbsolutePath());
		}
	}
}
