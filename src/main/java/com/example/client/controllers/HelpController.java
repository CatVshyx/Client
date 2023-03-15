package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.model.MessagePopUp;
import com.example.client.additional.ControllerExtension;
import com.example.client.service.HttpClientService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class HelpController implements ControllerExtension {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button cancelButton;
    @FXML
    private ImageView close;
    @FXML
    private TextField describeQuestion;
    @FXML
    private Button issueButton;
    @FXML
    private AnchorPane issuePane;
    @FXML
    private Button sendButton;
    @FXML
    private TextField titleQuestion;
    @FXML
    private AnchorPane underPane;
    @FXML
    private Label aboutProject;

    private final Image confirm = new Image(new File("src/main/resources/com/example/icons/storage/accept.png").toURI().toString());
    Label label;
    Button b;

    public void initialize(){
        HelloApplication.setHelpController(this);
        close.setOnMouseClicked(action -> HelloApplication.getSideBarController().removePane(issuePane));
        cancelButton.setOnAction(action -> HelloApplication.getSideBarController().removePane(issuePane));
        issueButton.setOnMouseClicked(action ->{
            issuePane.setVisible(true);
            underPane.setVisible(true);
            describeQuestion.setText(null);
            titleQuestion.setText(null);
            issuePane.getChildren().remove(b);
            issuePane.getChildren().remove(label);
            HelloApplication.getSideBarController().addPane(issuePane);
        });

        aboutProject.setOnMouseEntered(action -> aboutProject.setUnderline(true));
        aboutProject.setOnDragExited(action -> aboutProject.setUnderline(false));
        aboutProject.setOnMouseClicked(action -> openAboutProject());

        new MessagePopUp(issuePane);

        sendButton.setOnAction(actionEvent -> {
            String title = titleQuestion.getText();
            String description = describeQuestion.getText();
            if(description == null || title == null) return;
            if(description.length() < 4 || title.length() < 4) return;
            HashMap<String,String> mapa = new HashMap<>();
            mapa.put("title",title);
            mapa.put("description",description);
            try {
                HttpClientService.sendHelp(mapa);
                showConfirmFrame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void openAboutProject() {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("aboutProject.fxml"));
        AnchorPane pane;
        try {
            pane = loader.load();
            new MessagePopUp(pane);

            for (Node node : pane.getChildren()){

                if(node == null || node.getId() == null) continue;
                if (node.getId().equals("closeButton")) node.setOnMouseClicked(action -> HelloApplication.getSideBarController().removePane(pane));
                if(node.getId().equals("splitPane")){
                    SplitPane splitPane = (SplitPane) node;
                    splitPane.setMouseTransparent(false);
                    splitPane.lookupAll(".split-pane-divider").forEach(div ->  div.setMouseTransparent(true) );
                }
            }
            setPicture((Circle) loader.getNamespace().get("devDima"), "src/main/resources/com/example/icons/devDima.jpg");
            setPicture((Circle) loader.getNamespace().get("devVlad"), "src/main/resources/com/example/icons/devVlad.jpg");

            HelloApplication.getSideBarController().addPane(pane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showConfirmFrame(){
        label = new Label("Your question has been successfully sent. \n" +
                "The answer will be sent to your email within three working days.", new ImageView(confirm));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setLayoutY(150);
        label.setLayoutX(116);

        b = new Button("Ok");
        b.setOnAction(actionEvent -> HelloApplication.getSideBarController().removePane(issuePane));

        b.getStyleClass().add("buttonWhite");
        b.setMinWidth(140);
        b.setLayoutX(240);
        b.setLayoutY(250);

        underPane.setVisible(false);
        issuePane.getChildren().add(label);
        issuePane.getChildren().add(b);
    }
    private void setPicture(Circle circle,String path){
        Image image = new Image(new File(path).toURI().toString(),300,300,false,true);
        circle.setFill(new ImagePattern(image));
    }
    @Override
    public AnchorPane getFrame() {
        return mainPane;
    }
}
