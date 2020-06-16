package jumpygrof;

import javafx.scene.control.Alert;

public class GUIAlertBox {
    //pop out a window to show the error message
    public static void display(String warning,String detail){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(warning);
        alert.setContentText(detail);
        alert.showAndWait();
    }
}
