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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.univaq.disim.bpd.publicbillpostingservice.model.Bill;
import it.univaq.disim.bpd.publicbillpostingservice.model.CheckAvailabilityResponse;
import it.univaq.disim.bpd.publicbillpostingservice.model.PosterAvailabilityRequest;
import it.univaq.disim.bpd.publicbillpostingservice.model.User;
import it.univaq.disim.bpd.publicbillpostingservice.model.Zone;

@Component("requestFileWriterDelegate")
public class RequestFileWriter extends BaseDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(RequestFileWriter.class);

	@Autowired
	private Configuration freemarkerConfig;

	@Override
	public void execute(DelegateExecution execution) {
		User userData = getRequiredVariable(execution, "userData", User.class);
		PosterAvailabilityRequest posterAvailabilityRequest = getRequiredVariable(execution,
				"posterAvailabilityRequest", PosterAvailabilityRequest.class);
		Map<String, List<Zone>> selectedZones = getRequiredVariable(execution, "selectedZones", Map.class);
		CheckAvailabilityResponse checkAvailabilityResponse = getRequiredVariable(execution,
				"checkAvailabilityResponse", CheckAvailabilityResponse.class);
		Bill orderInformation = getRequiredVariable(execution, "orderInformation", Bill.class);

		Map<String, Object> model = new HashMap<>();
		model.put("userData", userData);
		model.put("posterAvailabilityRequest", posterAvailabilityRequest);
		model.put("selectedZones", selectedZones);
		model.put("checkAvailabilityResponse", checkAvailabilityResponse);
		model.put("orderInformation", orderInformation);

		try {
			Template template = freemarkerConfig.getTemplate("requestFile.ftl");

			boolean isRunningFromJar = RequestFileWriter.class.getProtectionDomain().getCodeSource().getLocation()
					.getPath().endsWith(".jar");

			String basePath = isRunningFromJar
					? Paths.get(RequestFileWriter.class.getProtectionDomain().getCodeSource().getLocation().toURI())
							.getParent().toString()
					: Paths.get(System.getProperty("user.dir")).getParent().toString();

			File outputDir = new File(basePath, "output");
			if (!outputDir.exists() && !outputDir.mkdirs()) {
				throw new IllegalStateException("Failed to create output directory at: " + outputDir.getAbsolutePath());
			}

			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
			String filename = "bill_" + timestamp + ".txt";
			File outputFile = new File(outputDir, filename);

			try (Writer out = new FileWriter(outputFile)) {
				template.process(model, out);
				logger.info("✅ File written to: {}", outputFile.getAbsolutePath());
			}

		} catch (Exception e) {
			logger.error("Failed to generate request file", e);
			throw new RuntimeException("Failed to generate request file", e);
		}
	}

}
