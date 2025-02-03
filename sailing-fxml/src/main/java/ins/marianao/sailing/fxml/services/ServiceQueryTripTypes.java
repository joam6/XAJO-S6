package ins.marianao.sailing.fxml.services;

import java.util.List;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;

import ins.marianao.sailing.fxml.manager.ResourceManager;

public class ServiceQueryTripTypes extends ServiceQueryBase<TripType> {
	
	private int Id;
	private String Category;
	private String Departures;
	private String Description;
	private int Duration;
	private int Places;
	private int Price;
	private String Title;
	
	public ServiceQueryTripTypes(int Id, String Category, String Description, int Duration, int Places, int Price, String Title) {
		this.Id = Id;
		this.Category = Category;
		this.Departures = Departures;
		this.Description = Description;
		this.Duration = Duration;
		this.Places = Places;
		this.Price = Price;
		this.Title = Title;
	}
	
	@Override
	protected List<TripType> customCall() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
