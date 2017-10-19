package smarthome.automation;

import java.util.List;

/**
 * Interface pour les objets capable de préparer un EventTrigger
 * 
 * @author Gregory
 *
 */
public interface EventTriggerPreparable {
	public List domainList(EventTrigger eventTrigger);
	public List actionList(EventTrigger eventTrigger);
	public List parameterList(EventTrigger eventTrigger);
	public String domainValue();
}
