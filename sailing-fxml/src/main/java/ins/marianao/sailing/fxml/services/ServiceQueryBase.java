package ins.marianao.sailing.fxml.services;

import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public abstract class ServiceQueryBase<T> extends Service<List<T>>{

	protected static final String PATH_QUERY_ALL = "find/all";

	/* Must implement */
	protected abstract List<T> customCall() throws Exception;
	
	@Override
	protected Task<List<T>> createTask() {
		return new Task<List<T>>() {
			@Override
			protected List<T> call() throws Exception {
				return customCall();
			}
		};
	}
}
