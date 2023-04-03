package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.Allows;
import com.example.client.model.User;
import com.example.client.additional.ControllerExtension;
import com.example.client.additional.PopUpUtility;
import com.example.client.service.AdministrationService;
import javafx.fxml.Initializable;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.json.JSONObject;

import static com.example.client.util.Helper.setPictureOnImage;

public class UserPopUpController extends PopUpUtility implements Initializable , ControllerExtension {
    @FXML
    private Text additionalInformation;
    @FXML
    private CheckBox admin_edit;
    @FXML
    private CheckBox admin_view;

    @FXML
    private Button cancelButton;

    @FXML
    private ImageView closeButton;

    @FXML
    private Pane confirmFrame;

    @FXML
    private Text dateOfCreation;

    @FXML
    private CheckBox finance_edit;

    @FXML
    private CheckBox finance_view;

    @FXML
    private Button okButton;

    @FXML
    private AnchorPane pane;

    @FXML
    private CheckBox promo_edit;

    @FXML
    private CheckBox promo_view;

    @FXML
    private Button saveButton;

    @FXML
    private Pane settingsPane;

    @FXML
    private CheckBox storage_edit;

    @FXML
    private CheckBox storage_view;

    @FXML
    private Circle userPhoto;

    @FXML
    private Text userRole;

    @FXML
    private Text username;
    private User user;
    private  List<CheckBox> checkboxes;
    @FXML
    private Text phone;
    @FXML
    private Text errorLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelloApplication.setUserPopUpController(this);
        setPane(pane);
        checkboxes =  new ArrayList<>(Arrays.asList(storage_edit,storage_view,admin_edit,admin_view,finance_edit,finance_view,promo_edit,promo_view));
        closeButton.setOnMouseClicked(action -> closeWindow());
        cancelButton.setOnAction(actionEvent -> closeWindow());
        saveButton.setOnAction(action -> saveChanges());
        okButton.setOnAction(action -> closeWindow());
    }
    private void saveChanges(){

        JSONObject object = new JSONObject();
        List<Allows> allows = new ArrayList<>();
        object.put("email",user.getEmail());
        checkboxes.forEach(checkBox -> {
            if (checkBox.isSelected()){
                allows.add(Allows.valueOf(checkBox.getId()));
            }
            object.put(checkBox.getId(),checkBox.isSelected());
        });
        Response response = AdministrationService.changeAllows(object.toString());
        if (response.getHttpCode() != 200){
            errorLabel.setVisible(true);
            errorLabel.setText(response.getDescription().toString());
            return;
        }
        user.setAllows(allows.toArray(Allows[]::new));
        showConfirmFrame(true);
    }
    private void showConfirmFrame(boolean answer){
        settingsPane.setVisible(!answer);
        confirmFrame.setVisible(answer);
    }
    public void setUser(User user){
        this.user = user;
        System.out.println(user.toString());
        setPopUpAllows(user.getAllows());
        username.setText(user.getName());
        dateOfCreation.setText(user.getDateOfCreation().format(DateTimeFormatter.ofPattern("MMM d uuuu")));
        additionalInformation.setText(user.getEmail());
        userRole.setText(user.getRole());
        phone.setText(user.getPhoneNumber());
        InputStream is = AdministrationService.getUserPhoto(user.getPhotos());
        setPictureOnImage(is,userPhoto);
        showConfirmFrame(false);
        errorLabel.setVisible(false);
    }
    private void setPopUpAllows(Allows[] allows) {
        checkboxes.forEach(checkBox -> checkBox.setSelected(false));
        for(Allows allow : allows){
            Optional<CheckBox> optional = checkboxes.stream()
                    .filter(box -> allow.name().equals(box.getId()))
                    .findAny();
            optional.get().setSelected(true);
        }
    }

    @Override
    public Pane getFrame() {
        return pane;
    }
}
