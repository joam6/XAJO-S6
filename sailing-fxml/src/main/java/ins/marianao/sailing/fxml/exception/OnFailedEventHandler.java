package ins.marianao.sailing.fxml.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.exception.ExceptionUtils;

import ins.marianao.sailing.fxml.ControllerMenu;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;

public class OnFailedEventHandler implements EventHandler<WorkerStateEvent> {

	private String source;
	private Map<Control, Object> toRestore;
	// TextField 	=> javafx.scene.control.Control  =>  javafx.scene.control.TextInputControl 	setText
	// TextArea		=> javafx.scene.control.Control  =>  javafx.scene.control.TextInputControl 	setText
	// ComboBox 	=> javafx.scene.control.Control  =>  javafx.scene.control.ComboBoxBase<T> 	setValue
	// DatePicker 	=> javafx.scene.control.Control  =>  javafx.scene.control.ComboBoxBase<T>  	setValue
	// Spinner 		=> javafx.scene.control.Control      SpinnerValueFactory   					setValue	PENDING
	// CheckBox 	=> javafx.scene.control.Control  =>  										setSelected

	public OnFailedEventHandler(String source) {
		this.source = source;
		this.toRestore = new HashMap<>();
	}
	
	public OnFailedEventHandler(String source, Map<Control, Object> toRestore) {
		this.source = source;
		this.toRestore = toRestore==null?new HashMap<>():toRestore;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void handle(WorkerStateEvent event) {
		Throwable e = event.getSource().getException();
		
		if (e instanceof SessionExpiredException) {
			ResourceManager.getInstance().getMenuController().logoffClick(null);
		}
		
		// Restore values
		for (Entry<Control, Object> entry : this.toRestore.entrySet()) {
			System.out.println("RESTORE " +entry.getValue()+" "+entry.getKey().getClass());
			if (entry.getKey() instanceof TextInputControl) ((TextInputControl) entry.getKey()).setText((String) entry.getValue());
			if (entry.getKey() instanceof ComboBoxBase) ((ComboBoxBase) entry.getKey()).setValue(entry.getValue());
			if (entry.getKey() instanceof CheckBox) ((CheckBox) entry.getKey()).setSelected((Boolean) entry.getValue());
		}
		
		ControllerMenu.showError(source, e.getMessage(), ExceptionUtils.getStackTrace(e));
	}

}
