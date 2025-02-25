package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ins.marianao.sailing.fxml.exception.OnFailedEventHandler;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import ins.marianao.sailing.fxml.services.ServiceQueryTripType;
import ins.marianao.sailing.fxml.utils.Formatters;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import javafx.util.Callback;
import javafx.util.Pair;

public class ControllerTripType implements Initializable {

	@FXML private TableView<TripType> tripTypeTable;
	@FXML private TableColumn<TripType, Number> colID;
	@FXML private TableColumn<TripType, Category> colCategoria; //Modificado, estaba declarado como String.
	@FXML private TableColumn<TripType, String> colDepartures;
	@FXML private TableColumn<TripType, String> colDescription;
	@FXML private TableColumn<TripType, Number> colDuration;
	@FXML private TableColumn<TripType, Integer> colMaxPlaza;
	@FXML private TableColumn<TripType, Double> colPrecio;
	@FXML private TableColumn<TripType, String> colTitulo;
	@FXML private ComboBox<Pair<String, String>> categorySelect;

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		// Configuración de ComboBox para categorías
		// Obtener la lista de categorías con sus traducciones
		// Crear la lista de categorías con sus traducciones
		List<Pair<String, String>> categories = Stream.of(Category.values())
		        .map(category -> new Pair<>(category.name(), resource.getString("text.Category." + category.name())))
		        .collect(Collectors.toList());

		// Agregar la opción "Todas las categorías" al principio de la lista
		categories.add(0, new Pair<>("ALL", resource.getString("text.Category.ALL"))); // Opción por defecto, puedes traducir "AllCategories" si lo deseas

		// Asignar la lista de categorías al ComboBox o similar
		categorySelect.setItems(FXCollections.observableArrayList(categories));

		// Establecer un conversor para la visualización de los elementos en el ComboBox
		categorySelect.setConverter(Formatters.getStringPairConverter("Category"));

		// Agregar un listener para recargar los tipos de viaje cuando se selecciona una categoría
		categorySelect.valueProperty().addListener((observable, oldValue, newValue) -> reloadTripTypes());

		// Obtener el nombre de la categoría traducido cuando la categoría es seleccionada
		categorySelect.valueProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue == null) {
		        // Si no se ha seleccionado nada, se maneja el valor null (opción por defecto)
		        reloadTripTypes();
		    } else if (newValue.getKey().equals("ALL")) {
		        // Si se seleccionó la opción "Todas las categorías"
		        reloadTripTypes();
		    /*
		     * ORIGINAL, 25/02 20:15
		     * } else { 
		        // Si se seleccionó una categoría específica
		        Category selectedCategory = Category.valueOf(newValue.getKey()); // Convierte la clave a un valor de la enumeración Category
		        String categoryName = resource.getString("text.Category." + selectedCategory.name());
		        if (categoryName == null || categoryName.isEmpty()) {
		            categoryName = selectedCategory.name(); // Valor por defecto si no se encuentra la traducción
		        }
		      */
		        
		     // MODIFICADO 25/02 20:21 - Filtrar por categoria.
		    } else if (newValue.getKey().equals("GROUP")){
			        // Si se seleccionó una categoría específica
			        Category selectedCategory = Category.valueOf(newValue.getKey()); // Convierte la clave a un valor de la enumeración Category
			        String categoryName = resource.getString("text.Category." + selectedCategory.name());
			        if (categoryName == null || categoryName.isEmpty()) {
			            categoryName = selectedCategory.name(); // Valor por defecto si no se encuentra la traducción
			        }
			        
		    } else if (newValue.getKey().equals("PRIVATE")){
		        // Si se seleccionó una categoría específica
		        Category selectedCategory = Category.valueOf(newValue.getKey()); // Convierte la clave a un valor de la enumeración Category
		        String categoryName = resource.getString("text.Category." + selectedCategory.name());
		        if (categoryName == null || categoryName.isEmpty()) {
		            categoryName = selectedCategory.name(); // Valor por defecto si no se encuentra la traducción
		        }
		        
		      
		        // Aquí puedes hacer algo con el nombre de la categoría si es necesario
		        reloadTripTypes();
		    }
		});

		/*List<Pair<String, String>> categories = Stream.of(TripType.Category.values())
				.map(category -> new Pair<>(category.name(), resource.getString("text.TripType." + category.name())))
				.collect(Collectors.toList());

		ObservableList<Pair<String, String>> listCategories = FXCollections.observableArrayList(categories);
		listCategories.add(0, null); // Agregar opción "Todas las categorías" al inicio

		this.categorySelect.setItems(listCategories);
		this.categorySelect.setConverter(Formatters.getStringPairConverter("Triptype"));

		// Agregar listener para recargar los tipos de viaje cuando cambia la selección
		this.categorySelect.valueProperty().addListener((observable, oldValue, newValue) -> reloadTripTypes());*/
		// Configuración de columnas en la tabla
		
		/*
		this.colCategoria.setCellValueFactory(cellData ->
		new SimpleStringProperty(resource.getString("text.Category." + cellData.getValue().getCategory().name()))
				);
	    this.colCategoria.setCellFactory(TextFieldTableCell.forTableColumn());
		 */
		
		
		this.colCategoria.setCellValueFactory(cellData ->  
		    new SimpleObjectProperty<>(cellData.getValue().getCategory())  
		);  

		this.colCategoria.setCellFactory(ComboBoxTableCell.forTableColumn(Category.values()));
		
	    this.colDepartures.setCellValueFactory(new PropertyValueFactory<>("departures"));
	    this.colDepartures.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
	    this.colDescription.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colDuration.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, Number>, ObservableValue<Number>>() {  
	        @Override  
	        public ObservableValue<Number> call(TableColumn.CellDataFeatures<TripType, Number> cell) {  
	            TripType tripType = cell.getValue();  
	            return new SimpleIntegerProperty(tripType.getDuration()); // Asumiendo que hay un método getDuration()  
	        }  
	    });   

        //this.colDuration.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter(Double.class)));

	    this.colMaxPlaza.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, Integer>, ObservableValue<Integer>>() {  
	        @Override  
	        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<TripType, Integer> cell) {  
	            TripType tripType = cell.getValue();  
	            return new SimpleIntegerProperty(tripType.getMaxPlaces()).asObject(); // Usa asObject() para convertir a ObservableValue<Integer>  
	        }  
	    });  
	    //this.colMaxPlaza.setCellFactory(TextFieldTableCell.forTableColumn());

	    //this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("price"));
	    this.colPrecio.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TripType, Double>, ObservableValue<Double>>() {  
	        @Override  
	        public ObservableValue<Double> call(TableColumn.CellDataFeatures<TripType, Double> cell) {  
	            TripType tripType = cell.getValue();  
	            // Asegúrate de que getPrice() devuelve un double o Double  
	            return new SimpleDoubleProperty(tripType.getPrice()).asObject(); // usa SimpleDoubleProperty  
	        }  
	    });  
	    
	    //this.colPrecio.setCellFactory(TextFieldTableCell.forTableColumn());

	    this.colTitulo.setCellValueFactory(new PropertyValueFactory<>("title"));
	    this.colTitulo.setCellFactory(TextFieldTableCell.forTableColumn());

	    // Recargar los tipos de viaje al inicio
	    this.reloadTripTypes();
	}

	private void reloadTripTypes() {
		Pair<String, String> selectedCategory = categorySelect.getValue();

		if (tripTypeTable == null) {
			System.err.println("La tabla tripTypeTable no está inicializada.");
			return;
		}

		// Desactivar la edición de la tabla mientras se cargan los datos
		tripTypeTable.setEditable(false);
		tripTypeTable.getItems().clear();  // Limpiar la tabla antes de añadir nuevos datos

		// Preparar el filtro de categorías
		Category[] categories = null;
		if (selectedCategory != null && selectedCategory.getKey() != null) {
			try {
				categories = new Category[]{Category.valueOf(selectedCategory.getKey())};
			} catch (IllegalArgumentException e) {
				System.err.println("Categoría no válida: " + selectedCategory.getKey());
				return; // Salir si la categoría no es válida
			}
		}

		// Iniciar la consulta para obtener los tipos de viaje
		final ServiceQueryTripType queryTripTypes = new ServiceQueryTripType();

		// Configurar el manejador de éxito para cuando la consulta termine
		queryTripTypes.setOnSucceeded(event -> {
			List<TripType> tripTypes = queryTripTypes.getValue();
			System.out.println("Fetched trip types: " + (tripTypes != null ? tripTypes.size() : 0));

			if (tripTypes != null) {
				ObservableList<TripType> observableTripTypes = FXCollections.observableArrayList(tripTypes);
				tripTypeTable.setItems(observableTripTypes);
			} else {
				System.err.println("No se encontraron tipos de viaje.");
			}
		});

		// Configurar el manejador de fallo para manejar errores en la consulta
		queryTripTypes.setOnFailed(new OnFailedEventHandler(
				ResourceManager.getInstance().getText("error.viewTripTypes.web.service")
				));

		// Iniciar la consulta
		queryTripTypes.start();
	}
}
