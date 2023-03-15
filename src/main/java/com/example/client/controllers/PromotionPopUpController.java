package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.Discount;
import com.example.client.additional.ControllerExtension;
import com.example.client.additional.PopUpUtility;
import com.example.client.model.Product;
import com.example.client.service.DiscountService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import static com.example.client.util.Helper.isFilled;
import static com.example.client.util.Helper.isNumeric;
import static com.example.client.util.Helper.isDateGreater;
public class PromotionPopUpController  extends PopUpUtility implements ControllerExtension {
    @FXML
    private AnchorPane addDiscPane;

    @FXML
    private Button saveChanges;

    @FXML
    private Button closeOK;

    @FXML
    private Label currency;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private Button deletePromotion;

    @FXML
    private TextField discountField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField productCategory;

    @FXML
    private TextField productName;

    @FXML
    private AnchorPane productPane;

    @FXML
    private AnchorPane showConfirm;

    @FXML
    private Text textDelete;

    @FXML
    private Label value;

    @FXML
    private ImageView x_close;

    @FXML
    private ImageView x_close2;
    @FXML
    private Label labelError;
    @FXML
    private Label labelConfirm;
    private Product currProduct;

    public void initialize(){
        super.setPane(productPane);
        HelloApplication.setPromotionPopUpController(this);
        x_close.setOnMouseClicked(mouseEvent -> closeWindow());
        x_close2.setOnMouseClicked(mouseEvent -> closeWindow());
        closeOK.setOnAction(action -> closeWindow());

        saveChanges.setOnAction(actionEvent -> saveDiscount());
        deletePromotion.setOnAction(actionEvent -> deleteDiscount());
    }
    private void deleteDiscount(){
        Response response = DiscountService.deleteDiscount(currProduct);
        System.out.println(response);
        if (response.getHttpCode() != 200){
            labelError.setVisible(true);
            labelError.setText(response.getDescription().toString());
            return;
        }

        showConfirmFrame("The discount was successfully deleted");
    }
    private void saveDiscount(){
        labelError.setVisible(true);

        if (!isFilled(new TextField[]{discountField,dateFrom.getEditor(),dateTo.getEditor()} )){labelError.setText("All the field are not filled");}
        else if(!isNumeric(discountField.getText())){labelError.setText("Check discount field. It must be a number");}
        else if(isDateGreater(dateFrom.getValue(),dateTo.getValue())){labelError.setText("Select the right dates");}
        else {
            float f = (100 - Integer.parseInt(discountField.getText() ) )/100f;
            Discount discount = new Discount(f,dateFrom.getValue(),dateTo.getValue());
            Response response = DiscountService.editDiscount(currProduct,discount);
            System.out.println(response + "  editing discount");
            if (response.getHttpCode() != 200){
                labelError.setVisible(true);
                labelError.setText(response.getDescription().toString());
                return;
            }
            showConfirmFrame("The discount has successfully been changed");
        }
    }
    private void showConfirmFrame(String text){

        labelConfirm.setText(text);
        showConfirm.setVisible(true);
        addDiscPane.setVisible(false);
    }
    public void setDiscount(Product product){
        this.currProduct = product;
        this.productName.setText(currProduct.getName());
        this.priceField.setText(String.valueOf(currProduct.getPrice()));
        Discount discount = currProduct.getDiscount();
        this.discountField.setText(String.valueOf( 100 - (int) (discount.getDiscountProperty()*100)));
        this.dateFrom.setValue(discount.getStartDate());
        this.dateTo.setValue(discount.getEndDate());
        this.productCategory.setText(currProduct.getCategory().name());

        showConfirm.setVisible(false);
        addDiscPane.setVisible(true);
        labelError.setVisible(false);
    }

    @Override
    public AnchorPane getFrame() {
        return productPane;
    }
}
