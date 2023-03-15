package com.example.client;

import com.example.client.controllers.*;
import com.example.client.service.HttpClientService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class HelloApplication extends Application {
    private static Stage stage;
    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 700;
    static SideBarController sideBarController;
    static FinanceController financeController;
    static StorageController storageController;
    static PromotionsController promotionsController;
    static PromotionPopUpController promotionPopUpController;
    static SettingsController settingsController;
    static UserPopUpController userPopUpController;
    static AddPromotionController addPromotionController;
    static HelpController helpController;
    static AdministrationController administrationController;

    static ProductEditController productEditController;
    private static void initializeFrames() throws IOException {
        // here are the frames which will be saved in memory
        FXMLLoader.load(HelloApplication.class.getResource("help_frame.fxml"));
        FXMLLoader.load(HelloApplication.class.getResource("frame_storage.fxml"));
        FXMLLoader.load(HelloApplication.class.getResource("finance_frame.fxml"));
        FXMLLoader.load(HelloApplication.class.getResource("frame_promotions.fxml"));
        FXMLLoader.load(HelloApplication.class.getResource("administration.fxml"));
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LogForm.fxml"));
        initializeFrames();
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        HelloApplication.stage = stage;
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
    @Override
    public void stop() throws Exception {
        File f = new File("src/main/resources/templates/");
        File[] temps = f.listFiles();
        if (temps.length != 0){
            for (File temp : temps){
                temp.delete();
            }
        }
        super.stop();
    }
    public static void loadLoginFrame(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LogForm.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
            stage.setScene(scene);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void loadMainApplication(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("frame_main.fxml"));
//            FXMLLoader.load(HelloApplication.class.getResource("settings.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
            stage.setScene(scene);

            getSideBarController().setFirstScene();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClientService service = new HttpClientService();
        service.getGreeting();
        launch();
    }
    public static void setSideBarController(SideBarController sideBarController1){
        sideBarController = sideBarController1;
    }
    public static SideBarController getSideBarController() {
        return sideBarController;
    }
    public static void setFinanceController(FinanceController cont){
        financeController = cont;
    }
    public static FinanceController getFinanceController(){
        return financeController;
    }
    public static StorageController getStorageController(){
        return storageController;
    }
    public static void setStorageController(StorageController storage){
        storageController = storage;
    }

    public static ProductEditController getProductEditController() {
        return productEditController;
    }

    public static void setProductEditController(ProductEditController productEditController) {
        HelloApplication.productEditController = productEditController;
    }
    public static PromotionsController getPromotionsController() {
        return promotionsController;
    }
    public static void setPromotionsController(PromotionsController promotionsController) {
        HelloApplication.promotionsController = promotionsController;
    }
    public static PromotionPopUpController getPromotionPopUpController() {
        return promotionPopUpController;
    }

    public static void setPromotionPopUpController(PromotionPopUpController promotionPopUpController) {
        HelloApplication.promotionPopUpController = promotionPopUpController;
    }
    public static AddPromotionController getAddPromotionController() {
        return addPromotionController;
    }

    public static void setAddPromotionController(AddPromotionController addPromotionController) {
        HelloApplication.addPromotionController = addPromotionController;
    }

    public static SettingsController getSettingsController() {
        return settingsController;
    }
    public static void setSettingsController(SettingsController settingsController) {
        HelloApplication.settingsController = settingsController;
    }
    public static HelpController getHelpController() {
        return helpController;
    }
    public static void setHelpController(HelpController helpController) {
        HelloApplication.helpController = helpController;
    }
    public static AdministrationController getAdministrationController() {
        return administrationController;
    }

    public static void setAdministrationController(AdministrationController administrationController) {
        HelloApplication.administrationController = administrationController;
    }
    public static UserPopUpController getUserPopUpController() {
        return userPopUpController;
    }

    public static void setUserPopUpController(UserPopUpController userPopUpController) {
        HelloApplication.userPopUpController = userPopUpController;
    }
}

