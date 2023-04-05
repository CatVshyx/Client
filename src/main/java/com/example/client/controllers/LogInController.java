package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.MessagePopUp;
import com.example.client.model.RegistrationRequest;
import com.example.client.service.HttpClientService;
import com.example.client.service.LogInService;
import com.example.client.service.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import org.json.JSONObject;

import java.net.URL;
import java.util.*;
import static com.example.client.controllers.SideBarController.*;

public class LogInController implements Initializable {
    @FXML
    private ImageView logoView;
    @FXML
    private PasswordField Sconfirm;

    @FXML
    private TextField Semail;

    @FXML
    private TextField Sname;

    @FXML
    private PasswordField Spassword;

    @FXML
    private TextField Sphone;

    @FXML
    private Pane animationPane;

    @FXML
    private Button buttonConfirm;

    @FXML
    private Pane buttonCreate;

    @FXML
    private Pane buttonJoin;

    @FXML
    private Button cancelCode;

    @FXML
    private Button cancelEmail;

    @FXML
    private Button cancelResetPassword;

    @FXML
    private Pane closePopUp;

    @FXML
    private Button confirmLogin;

    @FXML
    private Button confirmResetPassword;

    @FXML
    private TextField confirmResetedPassword;

    @FXML
    private Button continueCodePane;

    @FXML
    private Button continueEmailPane;

    @FXML
    private Button downloadButton;

    @FXML
    private Label errorInfo;

    @FXML
    private Label errorPaneWriteSendCode;

    @FXML
    private Label errorResetPasswordByCode;

    @FXML
    private Label errorSendEmailForCode;

    @FXML
    private TextField fieldEmailToSentdCode;

    @FXML
    private TextField fieldWriteSentCode;

    @FXML
    private Label forgotPasswordButton;

    @FXML
    private Hyperlink hpLink;

    @FXML
    private Pane joinPane;

    @FXML
    private Label labelErrorPopUp;

    @FXML
    private Label labelPopUp;

    @FXML
    private Button logOut;

    @FXML
    private TextField loginEmail;

    @FXML
    private Pane loginPane;

    @FXML
    private Label loginPaneError;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private AnchorPane loginRegPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField newResetedPassword;

    @FXML
    private Label onCreated;

    @FXML
    private Pane paneSendEmailForCode;

    @FXML
    private Pane paneWriteSentCode;

    @FXML
    private Pane popUpPane;

    @FXML
    private Button qCodeButton;

    @FXML
    private Label resendCodeButton;

    @FXML
    private Pane resetPasswordByCodePane;

    @FXML
    private Button signUpButton;

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField textfieldPopUp;

    private boolean Qstate = false;
    private TextField[] arr;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HelloApplication.setLoginController(this);
        configureJoinCreatePaneLogic();
        configurePaneSendEmailForCodeLogic();
        configurePaneWriteSentCode();
        configureResetPasswordByCodePane();
        setGradient();
        new MessagePopUp(popUpPane);
        forgotPasswordButton.setOnMouseClicked(action -> {
            paneSendEmailForCode.setVisible(true);
            loginPaneError.setVisible(false);
        });
        tabPane.getSelectionModel().getSelectedItem().setOnSelectionChanged(action -> {

            if (loginPaneError.isVisible())
                loginPaneError.setVisible(false);

        });
        downloadButton.setOnAction(action ->  SideBarController.openWebpage());
        qCodeButton.setOnAction(actionEvent -> setQCodeAnimation());
        hpLink.setOnAction(action -> openWebpage());
        cancelCode.setOnAction(actionEvent -> paneWriteSentCode.setVisible(false));
        confirmLogin.setOnAction(action -> login(loginEmail.getText(),loginPassword.getText()));
        onCreated.setOnMouseEntered(action -> onCreated.setVisible(false));
        signUpButton.setOnAction(action -> register());
        arr = new TextField[]{Sname, Sphone, Spassword, Semail,Sconfirm,loginEmail,loginPassword,textfieldPopUp,newResetedPassword,fieldEmailToSentdCode,fieldWriteSentCode,confirmResetedPassword};
    }
    private void login(String email, String password){
        loginPaneError.setVisible(true);
        if (loginPassword.getText().length()  < 3 || loginEmail.getText().length() < 5){
            loginPaneError.setText("Fill all the fields");
            return;
        }

        LogInService.login(email,password);
    }
    public void successfulLogin(){
        clearAllFields();
        System.out.println("successful login");
        joinPane.setVisible(true);
        if (Session.getMyCompany() != null){
            loadOnSuccess();
        }
    }
    public void failedOnLogin(String description){
        loginPaneError.setText(description.startsWith("<!") ? "Server is not running" : description);
        System.out.println(description);
    }
    private void configureJoinCreatePaneLogic(){
        logOut.setOnAction(action -> LogInService.logOut());
        buttonCreate.setOnMouseClicked(action -> {
            showHidePopUp(true);
            labelPopUp.setText("To create a company, write your company name");
            buttonConfirm.setOnAction(event -> {
                Response ans = LogInService.createCompany(textfieldPopUp.getText());
                if(ans.getHttpCode() != 200){
                    labelErrorPopUp.setVisible(true);
                    labelErrorPopUp.setText("Please rename the name of company");
                    return;
                }
                showHidePopUp(false);
                HttpClientService.setSessionWithServer();
                loadOnSuccess();
            });
        });
        buttonJoin.setOnMouseClicked(action -> {
            showHidePopUp(true);
            labelPopUp.setText("To join a company, put the invite code into field");
            labelErrorPopUp.setVisible(false);
            buttonConfirm.setOnAction(event -> {
                Response isSuccess = LogInService.joinCompany(textfieldPopUp.getText());
                if (isSuccess.getHttpCode() != 200){
                    labelErrorPopUp.setVisible(true);
                    labelErrorPopUp.setText("Please check the code of company");
                    return;
                }
                HttpClientService.setSessionWithServer();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                showHidePopUp(false);
                loadOnSuccess();

            });
        });
        closePopUp.setOnMouseClicked(action -> showHidePopUp(false));

    }
    private void configurePaneWriteSentCode(){
        // error - errorPaneWriteSendCode , code - fieldWriteSentCode
        continueCodePane.setOnAction(action -> {
            // writes code , on success change pane
            String code = fieldWriteSentCode.getText();
            errorPaneWriteSendCode.setVisible(true);
            if (code.length() < 2) {
                errorPaneWriteSendCode.setText("Too short for code");
                return;
            }
            errorPaneWriteSendCode.setVisible(false);
            paneWriteSentCode.setVisible(false);
            resetPasswordByCodePane.setVisible(true);
        });
        cancelCode.setOnAction(action -> {
            // clear email field hide all other panes and come back to login pane
            fieldEmailToSentdCode.clear();
            paneWriteSentCode.setVisible(false);
            resetPasswordByCodePane.setVisible(false);
            paneSendEmailForCode.setVisible(false);
        });
        resendCodeButton.setOnMouseClicked(action -> LogInService.forgotPassword(fieldEmailToSentdCode.getText()));
    }
    private void configureResetPasswordByCodePane(){
        // error - errorResetPasswordByCode
        // password confirm and new reseted passwords
        confirmResetPassword.setOnAction(action -> {
            // send new password and code as a p, on success i login and clear all the stuff
            String newPass = newResetedPassword.getText();
            String confirmPass = confirmResetedPassword.getText();
            errorResetPasswordByCode.setVisible(true);
            if (newPass.length() < 6){
                errorResetPasswordByCode.setText("The new password is too short");
                return;
            }
            if (!newPass.equals(confirmPass)){
                errorResetPasswordByCode.setText("The password are not equal, check them");
                return;
            }
            JSONObject object = new JSONObject();
            object.put("password",newPass);
            object.put("code",fieldWriteSentCode.getText());
            Response response = LogInService.resetPasswordByCode(object.toString());

            if (response.getHttpCode() != 200){
                errorResetPasswordByCode.setText(response.getDescription().toString());
                return;
            }
            clearAllFields();
            loginPaneError.setVisible(false);
            paneWriteSentCode.setVisible(false);
            resetPasswordByCodePane.setVisible(false);
            System.out.println("Successfully changed");

            login(fieldEmailToSentdCode.getText(),confirmPass);


        });
        cancelResetPassword.setOnAction(action -> {
            paneWriteSentCode.setVisible(true);
            resetPasswordByCodePane.setVisible(false);
        });
    }
    private void configurePaneSendEmailForCodeLogic(){
        // error info -  label errorSendEmailForCode
        continueEmailPane.setOnAction(actionEvent -> {
            String email = fieldEmailToSentdCode.getText();
            errorSendEmailForCode.setVisible(true);
            if(email.length() < 5) {
                redField(fieldEmailToSentdCode,true);
                return;
            }
            redField(fieldEmailToSentdCode,false);
            Response response = LogInService.forgotPassword(fieldEmailToSentdCode.getText());
            if (response.getHttpCode() != 200){
                errorSendEmailForCode.setText(response.getDescription().toString());
                return;
            }
            errorSendEmailForCode.setVisible(false);
            paneSendEmailForCode.setVisible(false);
            paneWriteSentCode.setVisible(true);
        });
        cancelEmail.setOnAction(actionEvent -> paneSendEmailForCode.setVisible(false));
    }
    private void showHidePopUp(boolean flag){
        popUpPane.setVisible(flag);
        loginRegPane.setOpacity(flag ? 0.3 : 1);
    }
    private void setGradient(){
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("rgba(223, 211, 245, 0.7)")),
                new Stop(1, Color.web("rgba(211, 243, 245, 0.7)")),
                new Stop(0.8, Color.web("rgba(227, 252, 218, 0.7)"))
        };
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        Rectangle rect = new Rectangle (0, 0, 634, 700);
        rect.setViewOrder(2);
        rect.setArcHeight(0);
        rect.setArcWidth(0);
        rect.setFill(lg1);

        animationPane.getChildren().add(rect);
    }
    private void redField(TextField n, boolean flag){
        if(flag){n.setStyle("-fx-border-color:#B00000;");}
        else{n.setStyle("-fx-border-color:rgba(113, 115, 177, 0.2);");}
    }
    private void setQCodeAnimation(){
        if(!Qstate){
            hpLink.setVisible(false);

            Qstate = true;
            scaleNode(qCodeButton,5,true);
            scaleNode(qCodeButton,6,false);
            translateNode(qCodeButton,60,true);
            translateNode(qCodeButton,80,false);
        }else{
            hpLink.setVisible(true);

            Qstate = false;
            scaleNode(qCodeButton,-5,true);
            scaleNode(qCodeButton,-6,false);
            translateNode(qCodeButton,-60,true);
            translateNode(qCodeButton,-80,false);
        }
    }
    private void clearAllFields(){
        Arrays.stream(arr).forEach(TextInputControl::clear);
    }
    private void register(){
        Arrays.stream(arr).forEach(field -> field.setStyle("-fx-border-color:rgba(113, 115, 177, 0.2);"));
        errorInfo.setVisible(true);
        if(!isFilled()) return;

        RegistrationRequest request = new RegistrationRequest(Sname.getText(),Semail.getText(),Spassword.getText(),Sphone.getText());
        Response response = LogInService.register(request);

        if (response.getHttpCode() != 200){
            errorInfo.setText((String) response.getDescription());
            return;
        }
        errorInfo.setVisible(false);
        clearAllFields();
        tabPane.getSelectionModel().select(0);
        onCreated.setVisible(true);

    }
    public Image getImageIcon(){
        return logoView.getImage();
    }

    private boolean isFilled(){
        if(Sname.getText().length() < 4 ) {
            errorInfo.setText("Error Description: The name is too short");
            redField(Sname,true);
        }
        else if(!Sphone.getText().contains("+38") || Sphone.getText().length() < 6){
            System.out.println("Add regex phone number validation pls");
            errorInfo.setText("Error Description: Invalid phone number");
            redField(Sphone,true);
        }
        else if(Semail.getText().length() < 6 || !Semail.getText().contains("@gmail.com")){
            errorInfo.setText("Error Description: Invalid email");
            redField(Semail,true);
        }else if(Spassword.getText().length() < 8){
            errorInfo.setText("Error Description: Password is too short");
            redField(Spassword,true);
        } else if (!Spassword.getText().equals(Sconfirm.getText())){
            errorInfo.setText("Error Description: Password are not equal");
            redField(Spassword,true);
        }else { return true; }

        return false;
    }
    private void loadOnSuccess(){
        HelloApplication.loadMainApplication();
    }

}
