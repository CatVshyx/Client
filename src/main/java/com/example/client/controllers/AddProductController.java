package com.example.client.controllers;

import com.example.client.additional.Response;
import com.example.client.model.Product;
import com.example.client.additional.Category;
import com.example.client.additional.ControllerExtension;
import com.example.client.additional.PopUpUtility;
import com.example.client.service.StorageService;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.json.JSONObject;
import static com.example.client.util.Helper.*;

public class AddProductController extends PopUpUtility implements ControllerExtension {
    @FXML
    private AnchorPane productPane;
    @FXML
    private Button addButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label currency;
    @FXML
    private TextField priceField;
    @FXML
    private TextField productCategory;
    @FXML
    private TextField productName;
    @FXML
    private TextField supplierField;
    @FXML
    private ImageView x_close;

    @FXML
    private TextField valueField;

    @FXML
    private Pane confirmPane;
    @FXML
    private Button closeOk;

    @FXML
    private Group group;

    @FXML
    private Label errorLabel;

    public void initialize(){
        super.setPane(productPane);

        addButton.setOnAction(actionEvent -> {
            errorLabel.setVisible(true);
            if(!onCheck()) return;
            Product product = new Product(productName.getText(),
                    Float.parseFloat(priceField.getText()),
                    Integer.parseInt(valueField.getText()),
                    Category.valueOf(productCategory.getText().toUpperCase()))
                    .setSupplier(supplierField.getText());
            addProduct(product);
        });
        closeOk.setOnAction(action -> closeWindow());
        x_close.setOnMouseClicked(action -> closeWindow());
        cancelButton.setOnAction(actionEvent -> closeWindow());
    }
    private boolean onCheck(){
        if(!isFilled(new TextField[]{priceField,productCategory,supplierField,productName,valueField})) {
            errorLabel.setText("Check all the fields to be filled");
            return false;
        }
        else if(!Category.checkCategory((productCategory.getText()))){
            errorLabel.setText("Check the correct category of the product");
            return false;
        }
        else if (!isFloat(priceField.getText()) || !isNumeric(valueField.getText())){
            errorLabel.setText("Check price and value fields. They must be numbers");
            return false;
        }
        return true;
    }
    private void  addProduct(Product product) {
        Response response = StorageService.addProduct(product);
        if (response.getHttpCode() != 200){
            errorLabel.setText(response.getDescription().toString());
            return;
        }
        JSONObject json = new JSONObject(response.getDescription().toString());
        int productID = Integer.parseInt(json.get("id").toString());
        product.setId(productID);
        group.setVisible(false);
        confirmPane.setVisible(true);
        errorLabel.setVisible(true);
    }

    @Override
    public AnchorPane getFrame() {
        return productPane;
    }
}
