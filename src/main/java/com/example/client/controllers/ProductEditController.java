package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.Discount;
import com.example.client.model.Product;
import com.example.client.additional.Category;
import com.example.client.additional.ControllerExtension;
import com.example.client.additional.PopUpUtility;
import com.example.client.service.DiscountService;
import com.example.client.service.StorageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.client.util.Helper.*;

public class ProductEditController extends PopUpUtility implements ControllerExtension {
    // there some frames - information frame where you can only look at the detailed info of the product and move to the other frames
    // and editor frame where you can edit information about the product
    @FXML
    private Button addPromoButton;

    @FXML
    private AnchorPane addPromoDelProdPane;

    @FXML
    private Label amount;

    @FXML
    private Label amountValue;

    @FXML
    private Button cancelB;

    @FXML
    private Button cancelDel;

    @FXML
    private Button cancelPromo;

    @FXML
    private Text categoryInfo;

    @FXML
    private AnchorPane categoryPane;

    @FXML
    private ImageView close;

    @FXML
    private Button currDel;

    @FXML
    private Label currency;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    @FXML
    private Button deleteButton;

    @FXML
    private AnchorPane deletePane;

    @FXML
    private TextField discount;

    @FXML
    private ImageView editButton;

    @FXML
    private AnchorPane editProductPane;

    @FXML
    private ListView<Category> listView;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Label price;

    @FXML
    private Label productName;

    @FXML
    private Button promoConfirm;

    @FXML
    private AnchorPane promotionPane;

    @FXML
    private Button saveChanges;

    @FXML
    private Label supplier;

    private Label[] array;


    private boolean isEditing = false;

    private Product currProduct;

    private String categoryLast;

    @FXML
    private Pane confirmPane;
    @FXML
    private Label labelConfirm;
    @FXML
    private Button closeOk;
    @FXML
    private Label errorLabel;
    @FXML
    private Label productIdTitle;
    public void initialize(){
        super.setPane(mainPane);
        HelloApplication.setProductEditController(this);
        addPromoButton.setOnAction(actionEvent -> setPromotionFrame(true) );


        close.setOnMouseClicked(mouseEvent -> closeWindow());
        editButton.setOnMouseClicked(mouseEvent -> {
            if(!isEditing){ openEditorFrame(); return;}
            closeEditorFrame();
        });
        closeOk.setOnAction(action -> closeWindow());

       deleteButton.setOnAction(actionEvent ->  setDeletingFrame(true));

       array = new Label[]{productName, price, amount, supplier};
       productName.setWrapText(true);
       productName.setMaxWidth(120);
       setDeleteActions();
       setDiscountActions();
       setEditingActions();
    }

    private void setInformLabel(String text){
        labelConfirm.setText(text);
        confirmPane.setVisible(true);
        editProductPane.setVisible(false);

    }
    private void openEditorFrame(){
        changeButtonImage("decline");
        for(Label label : array){
            label.setVisible(false);
            TextField textField = new TextField(label.getText());
            textField.setId("text_"+label.getId());
            textField.setLayoutX(label.getLayoutX()-15);
            textField.setLayoutY(label.getLayoutY()-10);
            textField.setMaxWidth(label.getWidth()+20);
            textField.setMinHeight(40);
            textField.setStyle(" -fx-background-color:white; -fx-background-radius:15; -fx-border-radius:15; -fx-border-color:rgba(113, 115, 177, 0.2);");
            editProductPane.getChildren().add(textField);
        }
        deletePane.setVisible(false);
        promotionPane.setVisible(false);
        saveChanges.setVisible(true);
        cancelB.setVisible(true);
        addPromoDelProdPane.setVisible(false);
        categoryPane.setVisible(true);
        categoryInfo.setText(categoryLast);
        isEditing = true;
    }
    private void setDeletingFrame(boolean flag){
        if(flag){
            deletePane.setVisible(true);
            addPromoDelProdPane.setVisible(false);
            return;
        }
        deletePane.setVisible(false);
        addPromoDelProdPane.setVisible(true);
    }
    private void setPromotionFrame(boolean flag){
        if(flag){
            addPromoDelProdPane.setVisible(false);
            promotionPane.setVisible(true);
            return;
        }
        addPromoDelProdPane.setVisible(true);
        promotionPane.setVisible(false);
    }
    private void setDeleteActions(){
        errorLabel.setVisible(false);
        currDel.setOnAction(actionEvent -> {
            Response response = StorageService.removeProduct(currProduct);
            if (response.getHttpCode() != 200){
                errorLabel.setVisible(true);
                errorLabel.setText(response.getHttpCode() == 403 ? "You dont have permissions" : response.getDescription().toString());
                return;
            }
            setInformLabel("This product has been deleted");
        });
        cancelDel.setOnAction(actionEvent -> setDeletingFrame(false));
    }
    private void setEditingActions(){
        saveChanges.setOnAction(actionEvent -> {
            List<TextField> fields = new ArrayList<>();
            editProductPane.getChildren().forEach( node -> { if(node instanceof TextField) fields.add((TextField) node); } );

            currProduct.setName(fields.get(0).getText());
            currProduct.setPrice(Float.parseFloat(fields.get(1).getText()));
            currProduct.setAmount(Integer.parseInt(fields.get(2).getText()));
            currProduct.setSupplier(fields.get(3).getText());
            currProduct.setCategory(Category.valueOf(categoryInfo.getText()));
            Response response = StorageService.editProduct(currProduct);
            if (response.getHttpCode() != 200){
                errorLabel.setVisible(true);
                errorLabel.setText(response.getHttpCode() == 403 ? "You dont have permissions to edit" : response.getDescription().toString());
                return;
            }
            setInformLabel("Changes have been saved");
            }
        );

        cancelB.setOnAction(actionEvent -> closeEditorFrame());
        listView.setItems(FXCollections.observableArrayList(Arrays.asList(Category.values())));
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, category, t1) -> categoryInfo.setText(observableValue.getValue().name()) );
    }
    private void setDiscountActions(){
        dateTo.setEditable(false);
        dateFrom.setEditable(false);
        cancelPromo.setOnAction( actionEvent -> setPromotionFrame(false));
        promoConfirm.setOnAction(actionEvent -> {
            errorLabel.setVisible(true);
            if (!isFilled(new TextField[]{discount,dateFrom.getEditor(),dateTo.getEditor()} )){errorLabel.setText("All the field are not filled");}
            else if(!isNumeric(discount.getText())){errorLabel.setText("Check discount field. It must be a number");}
            else if(isDateGreater(dateFrom.getValue(),dateTo.getValue())){errorLabel.setText("Select the right dates");}
            else {addDiscount();}

        });
    }
    private void addDiscount(){
        float f = (100 - Integer.parseInt(discount.getText() ) )/100f;
        currProduct.setDiscount(new Discount(f,dateFrom.getValue(),dateTo.getValue()));
        Response response = DiscountService.addDiscount(currProduct);

        if (response.getHttpCode() != 200){
            errorLabel.setText(response.getDescription().toString());
            return;
        }
        errorLabel.setVisible(true);

        dateTo.getEditor().setText(null);
        discount.setText(null);
        dateFrom.getEditor().setText(null);

        setInformLabel("This promotion has been added");
    }
    private void closeEditorFrame(){
        ObservableList<Node> list = editProductPane.getChildren();
        list.removeAll(list.stream()
                .filter(node -> (node instanceof TextField)).toList()
        );
        for(Label label : array){
            label.setVisible(true);
        }
        saveChanges.setVisible(false);
        cancelB.setVisible(false);
        addPromoDelProdPane.setVisible(true);
        categoryPane.setVisible(false);
        isEditing = false;
        changeButtonImage("pen");
    }

    private void changeButtonImage(String name){
        File f = new File("src/main/resources/com/example/icons/storage/" + (name.equals("pen") ? "pen.png" : "decline.png"));
        ImageView imageView = new ImageView(new Image(f.toURI().toString()));
        imageView.setScaleX(0.5);
        imageView.setScaleY(0.5);
        editButton.setImage(imageView.getImage());
    }
    public void setProduct(Product product){

        currProduct = product;
        this.productIdTitle.setText("Product id №"+product.getId());
        this.productName.setText( product.getName());
        this.price.setText( String.valueOf(product.getPrice()));
        this.currency.setText(Product.currency);
        this.amountValue.setText("kg");
        this.amount.setText(String.valueOf(product.getAmount()));
        this.supplier.setText(product.getSupplier());
        this.categoryInfo.setText(product.getCategory().name());
        categoryLast = product.getCategory().name();

        errorLabel.setVisible(false);
        closeEditorFrame();
        setDeletingFrame(false);
        setPromotionFrame(false);
        clearInformLabel();
    }
    private void clearInformLabel(){
        editProductPane.setVisible(true);
        confirmPane.setVisible(false);
        mainPane.setOnMouseClicked(null);
    }

    @Override
    public AnchorPane getFrame() {
        return mainPane;
    }
}
