/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof;

import javafx.scene.control.Alert;

/**
 *
 * @author USER
 */
public class GUIAlertBox {
    public static void display(String warning,String detail){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(warning);
        alert.setContentText(detail);
        alert.showAndWait();
    }
}
