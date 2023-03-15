package com.example.client.ui.cells;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.MessagePopUp;
import com.example.client.model.User;
import com.example.client.service.AdministrationService;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.File;

public class DeleteCell extends TableCell<User,Boolean> {
    private final Label deleteLabel = new Label("Delete user");
    private Pane pane;
    private Label error;
    public DeleteCell(){
        Pane imageView = generateImageView("src/main/resources/com/example/icons/storage/close.png",0.4f,0.4f);

        deleteLabel.getStyleClass().add("simpleHovering");
        deleteLabel.setStyle("-fx-text-fill:#7173B1; -fx-font-size:12px; ");
        deleteLabel.setGraphic(imageView);
        deleteLabel.setContentDisplay(ContentDisplay.LEFT);

        deleteLabel.setOnMouseClicked(action -> generateConfirmPane());
    }

    private void generateConfirmPane(){
        pane = new AnchorPane();
        pane.setPrefSize(400,200);
        pane.getStyleClass().add("custom-pane");

        Pane xClose = generateImageView("src/main/resources/com/example/icons/storage/close.png",0.6f,0.6f);
        xClose.setLayoutX(380);
        xClose.setOnMouseClicked(action -> HelloApplication.getSideBarController().removePane(pane));

        Button delete = generateButton("Delete",40,120);
        delete.setOnAction(action -> configureButtonAction(this.getTableRow().getItem()));
        Button cancel = generateButton("Cancel",200,120);
        cancel.setOnAction(action ->  HelloApplication.getSideBarController().removePane(pane));
        error = new Label();
        error.setLayoutX(40);
        error.setLayoutY(80);
        error.setVisible(false);
        error.setStyle("-fx-text-fill:#b00000;");


        Text t = new Text("  Are you sure you want to delete user %s?".formatted(this.getTableRow().getItem().getName()));
        t.setLayoutX(60);
        t.setLayoutY(50);

        pane.getChildren().addAll(xClose,delete,cancel,t,error);
        new MessagePopUp(pane);
        HelloApplication.getSideBarController().addPane(pane);
    }
    private void configureButtonAction(User user){
        Response response = AdministrationService.deleteUser(user);
        if (response.getHttpCode() != 200){
            error.setVisible(true);
            error.setText(response.getDescription().toString());
            return;
        }
        HelloApplication.getSideBarController().removePane(pane);
        HelloApplication.getAdministrationController().refreshTable();
    }
    private Pane generateImageView(String path, float scaleX, float scaleY){
        Pane p = new Pane();
        ImageView imageView = new ImageView(new Image(new File(path).toURI().toString()));
        imageView.setScaleX(scaleX);
        imageView.setScaleY(scaleY);
        p.getChildren().add(imageView);
        p.getStyleClass().add("simpleHovering");
        return p;
    }
    private Button generateButton(String name,int layoutX, int layoutY){
        Button b = new Button(name);

        b.setPrefSize(140,30);
        b.getStyleClass().add("buttonWhite");
        b.setLayoutX(layoutX);
        b.setLayoutY(layoutY);

        return b;
    }
    @Override
    protected void updateItem(Boolean aBoolean, boolean empty) {
        super.updateItem(aBoolean, empty);
        if(!empty){
            setGraphic(deleteLabel);
        }
    }
}
