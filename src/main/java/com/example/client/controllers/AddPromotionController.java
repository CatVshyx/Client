package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.Discount;
import com.example.client.model.Product;
import com.example.client.additional.ControllerExtension;
import com.example.client.additional.PopUpUtility;
import com.example.client.service.DiscountService;
import com.example.client.service.StorageService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import static com.example.client.util.Helper.*;

public class AddPromotionController extends PopUpUtility implements ControllerExtension {

    @FXML
    private Button addPromotion;

    @FXML
    private Button cancelButton;

    @FXML
    private Button closeOK;

    @FXML
    private Label currency;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private TextField priceField;

    @FXML
    private TextField productCategory;

    @FXML
    private TextField productName;

    @FXML
    private AnchorPane promotionPane;

    @FXML
    private Label value;

    @FXML
    private TextField discountField;
    @FXML
    private ImageView x_close;
    @FXML
    private ImageView x_close2;
    @FXML
    private AnchorPane addDiscPane;
    @FXML
    private AnchorPane showConfirm;

    @FXML
    private ImageView setProductButton;
    @FXML
    private Label labelError;
    private Product product;
    public void initialize(){
        super.setPane(promotionPane);
        HelloApplication.setAddPromotionController(this);
        addPromotion.setOnAction(actionEvent -> onCheck());
        closeOK.setOnAction(actionEvent -> closeWindow());
        x_close.setOnMouseClicked(action -> closeWindow());
        x_close2.setOnMouseClicked(action -> closeWindow());
        cancelButton.setOnAction(actionEvent -> closeWindow());
        setProductButton.setOnMouseClicked(action -> {
            if (!fillGaps()){
                labelError.setVisible(true);
                labelError.setText("Product not found");
            }else {
                labelError.setVisible(false);
            }
        });
        setProductButton.setOnMouseEntered(action -> setProductButton.setOpacity(1));
        setProductButton.setOnMouseExited(action -> setProductButton.setOpacity(0.35));
    }
    private void addNewPromotion(){
        float f = (100 - Integer.parseInt(discountField.getText() ) )/100f;
        Product product1 = new Product(product);
        product1.setDiscount(new Discount(f,dateFrom.getValue(),dateTo.getValue()));
        Response response = DiscountService.addDiscount(product1);
        System.out.println(response);
        if (response.getHttpCode() != 200){
            labelError.setVisible(true);
            labelError.setText(response.getDescription().toString());
            return;
        }
        labelError.setVisible(false);
        addDiscPane.setVisible(false);
        showConfirm.setVisible(true);
    }
    public void setDiscountFromProduct(Product product){
        this.productName.setText(product.getName());
        this.priceField.setText(String.valueOf(product.getPrice()));
        this.productCategory.setText(product.getCategory().name());
        discountField.setText("");
        dateFrom.setValue(null);
        dateTo.setValue(null);
        addDiscPane.setVisible(true);
        showConfirm.setVisible(false);
    }
    private Product findProduct(String name){
        return  isNumeric(name)
                ? StorageService.findProductById(Long.parseLong(name))
                : StorageService.findProductByName(name);
    }
    private boolean fillGaps(){
        product = findProduct(productName.getText());
        if(product == null) return false;

        productName.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        productCategory.setText(product.getCategory().name());
        return true;
    }
    private void onCheck(){
        labelError.setVisible(true);

        if(!isFilled(new TextField[]{productName,discountField,dateFrom.getEditor(),dateTo.getEditor()})){labelError.setText("All the fields are not filled");}
        else if (!fillGaps()){labelError.setText("Product not found");}
        else if(isDateGreater(dateFrom.getValue(),dateTo.getValue())){labelError.setText("Select the right dates");}
        else if(!isNumeric(discountField.getText())){labelError.setText("Check discount field. It must be a number");}
        else{addNewPromotion();}
    }
    @Override
    public AnchorPane getFrame() {
        return promotionPane;
    }
}
