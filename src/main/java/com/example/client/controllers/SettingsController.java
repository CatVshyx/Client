package com.example.client.controllers;
import com.example.client.HelloApplication;
import com.example.client.additional.ControllerExtension;
import com.example.client.additional.PopUpUtility;
import com.example.client.additional.Response;
import com.example.client.model.Allows;
import com.example.client.model.User;
import com.example.client.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;
import static com.example.client.util.Helper.setPictureOnImage;

public class SettingsController extends PopUpUtility implements ControllerExtension {
    @FXML
    private ImageView leaveCompanyButton;
    @FXML
    private Label admin_edit;

    @FXML
    private Label admin_view;
    @FXML
    private Label finance_edit;

    @FXML
    private Label finance_view;
    @FXML
    private Label promo_edit;

    @FXML
    private Label promo_view;
    @FXML
    private Label storage_edit;

    @FXML
    private Label storage_view;

    @FXML
    private Button cancelButton;

    @FXML
    private Button cancelChangePassword;

    @FXML
    private Pane changePasswordPane;

    @FXML
    private ImageView closeButton;

    @FXML
    private Pane confirmFrame;

    @FXML
    private TextField newPassField;

    @FXML
    private TextField currentPassField;

    @FXML
    private TextField email;

    @FXML
    private TextField confirmPassField;
    @FXML
    private Label changePasswordButton;
    @FXML
    private Button okButton;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button saveButton;

    @FXML
    private Button savePassword;
    @FXML
    private Pane settingsPane;

    @FXML
    private Circle userPhoto;

    @FXML
    private Text userRole;

    @FXML
    private TextField username;

    private boolean photo = false;
    private File f;
    @FXML
    private TextField phoneNumber;
    @FXML
    private Label errorMainPane;
    @FXML
    private Label labelPasswordError;

    public void initialize(){
        super.setPane(pane);
        setMe();
        HelloApplication.setSettingsController(this);
        configureMainPane();
        configureChangePasswordPane();
        okButton.setOnAction(actionEvent -> closeWindow());
    }
    private void configureMainPane(){
        closeButton.setOnMouseClicked(action -> closeWindow());
        cancelButton.setOnAction(actionEvent -> closeWindow());
        leaveCompanyButton.setOnMouseClicked(action -> {
            Response response = SettingsService.leaveCompany();
            if (response.getHttpCode() != 200){
                errorMainPane.setText(response.getDescription().toString());
                errorMainPane.setVisible(true);
                return;
            }
            errorMainPane.setVisible(false);
            LogInService.logOut();
        });
        saveButton.setOnAction(action -> {
            HashMap<String,String> map = new HashMap<>();
            User me = Session.getApplicationMe();
            if (phoneNumber.getText() == null){}
            else if (!phoneNumber.getText().equals(me.getPhoneNumber()) && phoneNumber.getText().length() >= 8) map.put("phone",phoneNumber.getText());

            if (!username.getText().equals(me.getName()) && username.getText().length() > 3) map.put("username",username.getText());

            Response response = SettingsService.changeUserCredentials(map);
            System.out.println(response.toString() +  map.size()+ map);
            if (response.getHttpCode() != 200){
                System.out.println("Is not ok");
                return;
            }
            if (photo)
                uploadPhoto(f.toPath().toString());

            HttpClientService.setSessionWithServer();

            confirmFrame.setVisible(true);
            settingsPane.setVisible(false);

        });
        userPhoto.setOnMouseEntered(action -> userPhoto.setOpacity(0.7));
        userPhoto.setOnMouseExited(action -> userPhoto.setOpacity(1));
        userPhoto.setOnMouseClicked(action -> {

            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
            int response = chooser.showSaveDialog(null);
            if (response != JFileChooser.APPROVE_OPTION) return;

            photo = true;
            f = chooser.getSelectedFile();
            Image image = new Image(f.toURI().toString(),300,300,false,true);
            userPhoto.setFill(new ImagePattern(image));

        });
        changePasswordButton.setOnMouseClicked(action -> {
            settingsPane.setVisible(false);
            changePasswordPane.setVisible(true);
        } );
    }
    private void configureChangePasswordPane(){
        cancelChangePassword.setOnAction(action -> {
            settingsPane.setVisible(true);
            changePasswordPane.setVisible(false);
        });
        savePassword.setOnAction(action -> {
            changePassword(currentPassField.getText(),confirmPassField.getText());

        });
    }
    private void changePassword(String current, String  confirmPassword){
        labelPasswordError.setVisible(true);
        if(!newPassField.getText().equals(confirmPassword) || confirmPassword.length() < 8){
            labelPasswordError.setText("New password and confirm password are not equal or they are too short");
            return;
        }
        Response response = SettingsService.changePassword(current,confirmPassword);
        if (response.getHttpCode() != 200){
            labelPasswordError.setText(response.getDescription().toString());
            return;
        }
        labelPasswordError.setVisible(false);
        changePasswordPane.setVisible(false);
        confirmFrame.setVisible(true);
        saveButton.fire();

    }
    private void uploadPhoto(String path){
        System.out.println("uploading");
        Runnable runnable = () -> {
            Response response = SettingsService.uploadPhoto(path);
            System.out.println(response);
        };
        new Thread(runnable,"photo").start();
        try(FileInputStream fis = new FileInputStream(f)) {HelloApplication.getSideBarController().setMe(fis);}
        catch (IOException e) {throw new RuntimeException(e);}
    }

    void setMe(){
        User me = Session.getApplicationMe();
        InputStream is = AdministrationService.getUserPhoto(me.getPhotos());
        setPictureOnImage(is,userPhoto);
        username.setText(me.getName() == null ? "Random" : me.getName());
        email.setText(me.getEmail());
        userRole.setText(me.getRole());
        phoneNumber.setText(me.getPhoneNumber());
        setAllows(me.getAllows());
    }
    private void setAllows(Allows[] allows) {
        if(allows == null) return;

        for(Allows allow : allows){
            Label optional = Stream.of(storage_edit,storage_view,admin_edit,admin_view,finance_edit,finance_view,promo_edit,promo_view)
                    .filter(box -> allow.name().equals(box.getId()))
                    .findAny().orElse(null);
            if(optional == null) continue;
            optional.setStyle("-fx-background-color:rgba(223, 211, 245, 0.3);  -fx-border-color:rgba(113, 115, 177, 0.7)");
        }
    }

    @Override
    public AnchorPane getFrame() {
        return pane;
    }

}

