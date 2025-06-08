package it.univaq.disim.bpd.publicbillpostingservice.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public class BaseDelegate {

	protected void ensureVariableExists(DelegateExecution execution, String name) {
		if (execution.getVariable(name) == null) {
			throw new IllegalStateException("Missing required process variable: " + name);
		}
	}

	protected <T> T getRequiredVariable(DelegateExecution execution, String name, Class<T> type) {
		ensureVariableExists(execution, name);
		Object value = execution.getVariable(name);
		try {
			return type.cast(value);
		} catch (ClassCastException e) {
			throw new IllegalStateException("Variable '" + name + "' is not of expected type: " + type.getSimpleName(),
					e);
		}
	}

}
