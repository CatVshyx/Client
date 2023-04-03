package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.User;
import com.example.client.service.AdministrationService;
import com.example.client.service.Session;
import com.example.client.ui.cells.DeleteCell;
import com.example.client.ui.cells.DotsCell;
import com.example.client.ui.cells.DoubleInfoCell;
import com.example.client.additional.ControllerExtension;
import com.example.client.util.Helper;
import com.example.client.util.SystemClock;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.json.JSONObject;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdministrationController implements ControllerExtension {
    @FXML
    private CheckBox admin_edit;

    @FXML
    private CheckBox admin_view;

    @FXML
    private Label amountUsers;
    @FXML
    private Label currentDate;

    @FXML
    private CheckBox dateBox;

    @FXML
    private CheckBox finance_edit;

    @FXML
    private CheckBox finance_view;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private CheckBox positionBox;

    @FXML
    private CheckBox promo_edit;

    @FXML
    private CheckBox promo_view;

    @FXML
    private CheckBox storage_edit;

    @FXML
    private CheckBox storage_view;

    @FXML
    private ImageView toDefaultUsers;

    @FXML
    private ToggleGroup positionToggle;
    @FXML
    private TableView<User> tableUsers;
    @FXML
    private Label copyLink;
    @FXML
    private AnchorPane invitationPane;
    @FXML
    private Button closeInvitation;
    @FXML
    private Button saveButton;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextField emailField;
    @FXML
    private Pane invitePane;
    @FXML
    private Label labelError;

    boolean isDefault = true;
    List<CheckBox> boxes;
    public void initialize(){
        HelloApplication.setAdministrationController(this);
        setTableColumns();
        setSortingByDate();
        SystemClock.addDate(currentDate);
        buttonCancel.setOnAction(actionEvent -> clearInvite());
        toDefaultUsers.setOnMouseClicked(action -> setToDefaultUsers());
        menuBar.getMenus().forEach(menu -> menu.setOnAction(action -> setSortingByPosition()));
        copyLink.setOnMouseClicked(action -> setOnClipboard());
        closeInvitation.setOnAction(action -> setInvitationAnimation(false));
        saveButton.setOnAction(action -> {
            if(emailField.getText().length() < 6) return;
            sendInvitation();
        });

        boxes = Arrays.asList(storage_edit,storage_view,admin_edit,admin_view,finance_edit,finance_view,promo_edit,promo_view);
    }
    private void sendInvitation(){
        JSONObject object = new JSONObject();
        object.put("email",emailField.getText());
        RadioMenuItem radioMenuItem = (RadioMenuItem)positionToggle.getSelectedToggle();
        object.put("role",radioMenuItem.getText());
        boxes.stream().filter(CheckBox::isSelected).forEach(checkBox -> object.put(checkBox.getId(),"true"));

        Response serverAnswer = AdministrationService.sendInvitation(object.toString());
        if(serverAnswer.getHttpCode() != 200) {
            labelError.setVisible(true);
            labelError.setText(serverAnswer.getDescription().toString());
        }else{
            labelError.setVisible(false);
            invitationPane.setViewOrder(0);
            setInvitationAnimation(true);
            clearInvite();
        }
    }
    private void setInvitationAnimation(boolean value){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500),invitationPane);
        fadeTransition.setFromValue(value ? 0 : 1);
        fadeTransition.setToValue(value ? 1 : 0);
        fadeTransition.play();
        if(value) return;
        fadeTransition.setOnFinished(actionEvent -> invitationPane.setViewOrder(2));
    }
    public void removeUserFromTable(User user){
        tableUsers.getItems().remove(user);
        tableUsers.refresh();
    }
    private void clearInvite(){
        emailField.setText(null);
        boxes.forEach(box -> box.setSelected(false));
    }
    void sortBySearch(String name){
        if(!isDefault) setToDefaultUsers();
        Stream<User> productStream = AdministrationService.getAllUsers().stream();
        List<User> sorted = productStream.filter( user -> user.getName().contains(name)).collect(Collectors.toList());
        tableUsers.setItems(FXCollections.observableArrayList(sorted));

        tableUsers.refresh();
    }
    public void clearData(){
        tableUsers.getItems().clear();
    }
    private void setTableColumns(){
        tableUsers.setPlaceholder(Helper.getNoContentLabel());

        TableColumn<User,String> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>("userInfo"));
        column.setCellFactory(callback -> new DoubleInfoCell<>(true));
        column.setMinWidth(125);
        tableUsers.getColumns().add(column);

        column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>("userLevel"));
        column.setCellFactory(callback -> new DoubleInfoCell<>(true));
        tableUsers.getColumns().add(column);

        column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<>("userContacts"));
        column.setCellFactory(callback -> new DoubleInfoCell<>(true));
        tableUsers.getColumns().add(column);

        TableColumn<User,Boolean> actionButtons = new TableColumn<>();
        actionButtons.setCellFactory(callback -> new DotsCell<>());
        actionButtons.setMinWidth(105);
        actionButtons.setStyle("-fx-translate-x:-30px;");
        tableUsers.getColumns().add(actionButtons);

        actionButtons = new TableColumn<>();
        actionButtons.setCellFactory(callback -> new DeleteCell());
        actionButtons.setStyle("-fx-translate-x:-15px;");
        tableUsers.getColumns().add(actionButtons);
    }
    private void setSortingByDate(){
        dateBox.selectedProperty().addListener(action -> {
            List<User> sortedProducts = tableUsers.getItems()
                    .stream()
                    .sorted(Comparator.comparing(User::getDateOfCreation))
                    .collect(Collectors.toList());

            if(dateBox.isSelected()) Collections.reverse(sortedProducts);
            tableUsers.setItems(FXCollections.observableArrayList(sortedProducts));
        });
    }

    private void setSortingByPosition(){
        isDefault = false;
        RadioMenuItem menuItem = (RadioMenuItem) positionToggle.getSelectedToggle();
        Stream<User> stream = AdministrationService.getAllUsers().stream().filter(user -> user.getRole().equals(menuItem.getText()));

        tableUsers.setItems(FXCollections.observableArrayList(stream.toList()));
        tableUsers.refresh();
    }
    private void setToDefaultUsers(){
        isDefault = true;
        positionToggle.selectToggle(null);
        tableUsers.setItems(FXCollections.observableArrayList(AdministrationService.getAllUsers()));
        tableUsers.refresh();
    }
    private void setOnClipboard(){
        // INVITE LINK TO THE COMPANY - I GET FROM THIS COMPANY
        StringSelection stringSelection = new StringSelection(Session.getMyCompany().getInviteLink());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
    public void refreshTable(){
        tableUsers.refresh();
    }
    @Override
    public AnchorPane getFrame() {
        return mainPane;
    }

    public void setData(List<User> users) {
        System.out.println("setting data");
        amountUsers.setText(String.valueOf(users.size()));
        tableUsers.setItems(FXCollections.observableArrayList(users));
        tableUsers.refresh();
    }
}
