package ru.skysoftlab.greenhouse.common;

/**
 * Слушатель изменения состояния конька.
 * 
 * @author Lokot
 *
 */
public interface GableStateListener {

	/**
	 * Оповещвет о новом состоянии.
	 * 
	 * @param gableState
	 */
	public void gableStateIs(GableState gableState);
	
	
}
