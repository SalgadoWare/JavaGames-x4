package es.ucm.fdi.tp.view.gui.settings;

/**
 * GameWindow (the frame) has a reference to this interface
 * @author alex
 *
 */
public interface SettingsUse {
	public int getTimerValue();

	public int getThreadsValue();

	public void setAIthought(boolean bool);
	
	public void setEnabled(boolean enable);

	public void IAbuttonWasPressed();
}
