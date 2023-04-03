package com.example.client.controllers;

import com.example.client.HelloApplication;

import com.example.client.additional.Response;
import com.example.client.model.MessagePopUp;
import com.example.client.model.User;
import com.example.client.service.AdministrationService;
import com.example.client.service.LogInService;
import com.example.client.service.Session;
import com.example.client.service.StorageService;
import com.example.client.util.ConverterFactory;
import com.example.client.util.PropertyUtil;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import static com.example.client.util.Helper.setPictureOnImage;

public class SideBarController {
    @FXML
    private Pane addFinanceButton;

    @FXML
    private AnchorPane addFinancePane;

    @FXML
    private Button administration;

    @FXML
    private BorderPane borderPane;

    @FXML
    private BorderPane bpMain;

    @FXML
    private Button buttonAddProduct;

    @FXML
    private Button buttonAddPromotion;

    @FXML
    private Button cancel;

    @FXML
    private Circle circle;

    @FXML
    private Pane closeAddFinance;

    @FXML
    private Button closeFinanceAdd;

    @FXML
    private TextField currencyRate;

    @FXML
    private Button downloadButton;

    @FXML
    private Label errorOnCurrency;

    @FXML
    private TextField fieldFinance;

    @FXML
    private TextField fieldSoldAmount;

    @FXML
    private Button finance;

    @FXML
    private ToggleGroup financeGroup;

    @FXML
    private HBox hBoxFinance;

    @FXML
    private Button help;

    @FXML
    private Hyperlink hpLink;

    @FXML
    private Label infoDown;

    @FXML
    private Button log;

    @FXML
    private Button logOut;

    @FXML
    private AnchorPane log_out_frame;

    @FXML
    private ImageView loop;

    @FXML
    private AnchorPane paneSellProduct;

    @FXML
    private Button promotions;

    @FXML
    private Button qCodeButton;

    @FXML
    private AnchorPane scene;

    @FXML
    private TextField searchField;

    @FXML
    private Button sellButton;

    @FXML
    private Button settings;

    @FXML
    private Button storage;

    @FXML
    private AnchorPane topPane;

    @FXML
    private ImageView uploadCurrency;

    @FXML
    private Label username;

    @FXML
    private Pane wrappedSearch;
    @FXML
    private Label errorOnSell;
    @FXML
    private ImageView closeSellPane;
    @FXML
    private Text textLogOutSpecified;
    private static byte flag = 0;
    private boolean Qstate = false;
    private static Property<String> searched = new SimpleStringProperty();
    private char position = 's';
    private int productId = -1;

    public void initialize(){
        HelloApplication.setSideBarController(this);
        setMe(AdministrationService.getUserPhoto(Session.getApplicationMe().getPhotos()));
        configureFrameChangeButtons();
        configureAddFinancePane();
        configureSellPane();
        logOut.setOnMouseClicked(action -> {
            setSceneOpacity(0.3f);
            log_out_frame.setVisible(true);
            if (action.getButton().equals(MouseButton.PRIMARY)){
                textLogOutSpecified.setText("Are you sure you want to log out?");
                log.setText("Log out");
                log.setOnAction(logAction -> LogInService.logOut());
            }else{
                textLogOutSpecified.setText("Are you sure you want to quit?");
                log.setText("Quit");
                log.setOnAction(quitAction -> {
                    Stage stage = (Stage) log.getScene().getWindow();
                    stage.close();
                });
            }
        });
        downloadButton.setOnAction(actionEvent -> openWebpage());
        cancel.setOnAction(actionEvent -> {
            setSceneOpacity(1);
            log_out_frame.setVisible(false);
        });
        qCodeButton.setOnMouseExited(action -> {
            if(Qstate){
                hpLink.setVisible(true);
                infoDown.setVisible(true);
                Qstate = false;
                scaleNode(qCodeButton,-3,true);
                scaleNode(qCodeButton,-4,false);
                translateNode(qCodeButton,-50,true);
                translateNode(qCodeButton,40,false);

            }
        });
        qCodeButton.setOnAction(actionEvent -> {
            if(!Qstate){
                hpLink.setVisible(false);
                infoDown.setVisible(false);
                Qstate = true;
                scaleNode(qCodeButton,3,true);
                scaleNode(qCodeButton,4,false);
                translateNode(qCodeButton,50,true);
                translateNode(qCodeButton,-40,false);
            }else{
                hpLink.setVisible(true);
                infoDown.setVisible(true);
                Qstate = false;
                scaleNode(qCodeButton,-3,true);
                scaleNode(qCodeButton,-4,false);
                translateNode(qCodeButton,-50,true);
                translateNode(qCodeButton,40,false);
            }
        });

        buttonAddPromotion.setOnAction(actionEvent -> setPopUp("addPromotionMomento.fxml"));
        buttonAddProduct.setOnAction(actionEvent -> setPopUp("addProductMoment.fxml"));
        addFinanceButton.setOnMouseClicked(action -> {
            addFinancePane.setVisible(true);
            setSceneOpacity(0.3f);
        });



        searchField.textProperty().addListener((obs,old,newValue) ->{

            switch (position) {
                case 'a' -> HelloApplication.getAdministrationController().sortBySearch(newValue);
                case 'p' -> HelloApplication.getPromotionsController().sortBySearch(newValue);
                case 's' -> HelloApplication.getStorageController().sortBySearch(newValue);
            }
        });
        searchField.textProperty().bindBidirectional(searched);
        searchField.setOnMouseClicked( action -> {
            //all the movements
            switch(flag){
                case 0:

                    scaleNode(searchField,1,true);
                    searchField.setStyle(" -fx-background-color:transparent; -fx-border-color:rgba(113, 115, 177, 0.5); -fx-border-radius:7px;");
                    bpMain.requestFocus();
                    searchField.setPromptText("");
                    // translate Field <-
                    translateNode(searchField, -65,true);
                    //loop <-
                    translateNode(loop,-225,true);
                    flag = 1;
                    break;
                case 1:
                    loop.setVisible(false);
                    searchField.setStyle("-fx-font-size:10px;");
                    searchField.setStyle(" -fx-background-color:transparent; -fx-border-color:rgba(113, 115, 177, 0.5); -fx-border-radius:7px;");

                    // later i can set text into variable
                    flag = 2;
                    break;
                case 2:
                    scaleNode(searchField,-1,true);
                    searchField.setStyle("-fx-background-color:rgba(223, 211, 245, 0.5); -fx-border-color:rgba(113, 115, 177, 0.5); -fx-border-radius:12px;");
                    // translate ->
                    searchField.setText("");
                    searchField.setPromptText("Search...");
                    bpMain.requestFocus();
                    translateNode(searchField, 65,true);
                    //loop ->
                    loop.setTranslateX(0);
                    loop.setVisible(true);
                    flag = 0;
                    break;
            }
        });
    }
    private void configureSellPane(){
        new MessagePopUp(paneSellProduct);
        sellButton.setOnAction(action -> {
            JSONObject object = new JSONObject();
            object.put("id",productId);
            object.put("sold",fieldSoldAmount.getText());
            System.out.println(object);
            Response response = StorageService.sellProduct(object.toString());
            if (response.getHttpCode() != 200){
                errorOnSell.setVisible(true);
                errorOnSell.setText(response.getDescription().toString());
                return;
            }
            fieldSoldAmount.clear();
            paneSellProduct.setVisible(false);
            this.setSceneOpacity(1);
        });
        closeSellPane.setOnMouseClicked(action -> {
            paneSellProduct.setVisible(false);
            this.setSceneOpacity(1);
        });
    }
    private void configureFrameChangeButtons(){
        finance.setOnAction(actionEvent -> {
            buttonAddProduct.setVisible(false);
            buttonAddPromotion.setVisible(false);
            hBoxFinance.setVisible(true);
            wrappedSearch.setVisible(false);
            bpMain.setCenter(HelloApplication.getFinanceController().getFrame());
        });
        help.setOnAction(actionEvent -> {
            buttonAddProduct.setVisible(false);
            buttonAddPromotion.setVisible(false);
            hBoxFinance.setVisible(false);
            wrappedSearch.setVisible(false);
            bpMain.setCenter(HelloApplication.getHelpController().getFrame());
        });
        administration.setOnAction(actionEvent -> {
            buttonAddProduct.setVisible(false);
            buttonAddPromotion.setVisible(false);
            hBoxFinance.setVisible(false);
            wrappedSearch.setVisible(true);
            bpMain.setCenter(HelloApplication.getAdministrationController().getFrame());
            position = 'a';
        });
        storage.setOnAction(actionEvent -> {
            bpMain.setCenter(HelloApplication.getStorageController().getFrame());
            position = 's';
            buttonAddProduct.setVisible(true);
            hBoxFinance.setVisible(false);
            wrappedSearch.setVisible(true);
            buttonAddPromotion.setVisible(false);
        });
        settings.setOnAction(actionEvent -> setPopUp("settings.fxml")
        );
        promotions.setOnAction(actionEvent -> {
            bpMain.setCenter(HelloApplication.getPromotionsController().getFrame());
            position = 'p';
            buttonAddProduct.setVisible(false);
            wrappedSearch.setVisible(true);
            hBoxFinance.setVisible(false);
            buttonAddPromotion.setVisible(true);
        });
        circle.setOnMouseClicked(action-> setPopUp("settings.fxml"));
    }
    public  void openSellPane(int local){
        this.productId = local;

        if (errorOnSell.isVisible()){ errorOnSell.setVisible(true);}
        paneSellProduct.setVisible(true);
    }
    static void translateNode(Node node, int value, boolean direction){
        //true - x direction , false - y direction
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(node);
        translate.setDuration(Duration.millis(300));
        if(direction) {
            translate.setByX(value);
        }else{
            translate.setByY(value);
        }
        translate.play();
    }
    private void configureHBox(){
        for(Node child : hBoxFinance.getChildren()){
            if(child instanceof RadioButton){
                child.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton() != MouseButton.SECONDARY){ chooseCurrency(((RadioButton) child).getText()); }
                    else{hBoxFinance.getChildren().remove(child);}
                });
            }
        }
        hBoxFinance.getChildren().forEach(node -> {
            if(node instanceof RadioButton button){
                if(button.getText().equals("UAH")) {button.setSelected(true);}
            }

        });
    }
    void setMe(InputStream inputStream){
        username.setText(Session.getApplicationMe().getName());
        setPictureOnImage(inputStream,circle);
        circle.setOnMouseEntered(action -> circle.setOpacity(0.7));
        circle.setOnMouseExited(action -> circle.setOpacity(1));
    }
    static void scaleNode(Node node, int x, boolean direction){
        //true - x , false - y
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(node);
        scale.setDuration(Duration.millis(300));
        if(direction) {
            scale.setByX(x);
        }else{
            scale.setByY(x);
        }
        scale.setByX(x);
        scale.setAutoReverse(false);
        scale.play();
    }
    static void openWebpage() {
        try{
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(PropertyUtil.read("apple.link")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setFirstScene() {
        // delete as a method and create a method with all frame-work-buttons or that kind of this to organize my class
            bpMain.setCenter(HelloApplication.getStorageController().getFrame());
            buttonAddProduct.setVisible(true);
    }
    public AnchorPane getScene(){
        return scene;
    }
    public void setSceneOpacity(float opacity){
        borderPane.setOpacity(opacity);
    }
    public void setPopUp(String name){
        // sets the popup frame on scene right on the center of the window
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(name));
        AnchorPane pane;
        try {
            pane = loader.load();
            scene.getChildren().add(pane);
            setSceneOpacity(0.3f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void removePane(Pane pane){
        this.scene.getChildren().remove(pane);
        this.setSceneOpacity(1);
    }
    public void addPane(Pane pane){
        this.scene.getChildren().add(pane);
        this.setSceneOpacity(0.3f);
    }
    private void configureAddFinancePane(){
        new MessagePopUp(addFinancePane);
        configureHBox();
        closeAddFinance.setOnMouseClicked(action -> closeAddFinance());
        uploadCurrency.setOnMouseClicked(action -> currencyRate.setText(String.valueOf(Math.random()*6)));
        closeFinanceAdd.setOnAction(action -> {
            errorOnCurrency.setVisible(true);
            if(!checkCurrencyLogic()) return;

            addNewCurrency(fieldFinance.getText().toUpperCase());
            closeAddFinance();
        });
    }
    private boolean checkCurrencyLogic(){
        if(fieldFinance.getText().length() != 3){
            errorOnCurrency.setText("Name of the currency is 3 letters");
            return false;
        }
        else if(hBoxFinance.getChildren().size() == 6){
            errorOnCurrency.setText("The size can`t be more than 5");
            // idea to bind visible property of label errors and their panes - so they can hide by themselves
            return false;
        }
        return true;
    }
    private void closeAddFinance(){
        errorOnCurrency.setVisible(false);
        addFinancePane.setVisible(false);
        fieldFinance.setText("");
        currencyRate.setText("");
        setSceneOpacity(1);
    }
    private void chooseCurrency(String name){
        //  make it observable to my revenues and dates
        FinanceController.setCurrentCurrency( ConverterFactory.getCurrency(name));

    }
    private void addNewCurrency(String name){
        RadioButton newCurrency = new RadioButton(name);
        newCurrency.setPrefSize(60,30);
        newCurrency.getStyleClass().addAll("buttonWhite","custom-finance-box");

        newCurrency.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY){ chooseCurrency(newCurrency.getText()); }
            else{hBoxFinance.getChildren().remove(newCurrency);}
        });
        ConverterFactory.getCurrency(name);
        financeGroup.getToggles().add(newCurrency);
        hBoxFinance.getChildren().add(hBoxFinance.getChildren().size() - 1,newCurrency);
    }
}
