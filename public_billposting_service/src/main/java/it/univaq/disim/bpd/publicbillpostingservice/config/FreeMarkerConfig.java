package it.univaq.disim.bpd.publicbillpostingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

@org.springframework.context.annotation.Configuration
public class FreeMarkerConfig {

	@Bean
	@Primary
	public Configuration freemarkerConfig() {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
		cfg.setClassForTemplateLoading(this.getClass(), "/templates");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		return cfg;
	}
}