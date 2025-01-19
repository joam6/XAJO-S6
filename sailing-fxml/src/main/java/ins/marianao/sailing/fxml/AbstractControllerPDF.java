package ins.marianao.sailing.fxml;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.exception.ExceptionUtils;

import ins.marianao.sailing.fxml.manager.PdfManager;
import ins.marianao.sailing.fxml.manager.ResourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;


public abstract class AbstractControllerPDF implements Initializable {
	@FXML protected Button btnPDF;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resource) {
		ControllerMenu.addIconToButton(this.btnPDF, new Image(getClass().getResourceAsStream("resources/pdf-logo.png")), 30);
	}

	@FXML
	public void generarPDFClick(ActionEvent event) {
		try {
            PdfManager manager;

			String path = ResourceManager.BASE_DIR+"/"+documentFileName();
			
			manager = new PdfManager(path);

			manager.generarPDF(documentTitle(), htmlContentToPDF(), isDocumentLandscape());

			/*File pdfGenerat = new File(path);

			HostServices hostServices = ResourceManager.getInstance().getApp().getHostServices();
	        hostServices.showDocument(pdfGenerat.getAbsolutePath());*/
	        
	        ControllerMenu.showInfo(ResourceManager.getInstance().getText("fxml.text.viewUsers.report.title"), 
	        						ResourceManager.getInstance().getText("info.pdf.title")+" '"+ResourceManager.BASE_DIR+"'" );
	        
		} catch (Exception e) {
			//e.printStackTrace();
		    ControllerMenu.showError(ResourceManager.getInstance().getText("error.pdf.title"), e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
	}

	
	protected abstract String htmlContentToPDF() throws Exception;
	
	protected abstract String documentTitle();
	
	protected abstract String documentFileName();
	
	protected boolean isDocumentLandscape() {
		return false;
	}
}



