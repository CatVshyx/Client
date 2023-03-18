package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.service.DiscountService;
import com.example.client.service.StorageService;
import com.example.client.ui.cells.DotsCell;
import com.example.client.ui.cells.DoubleInfoCell;
import com.example.client.additional.Category;
import com.example.client.model.*;
import com.example.client.ui.cells.DateCell;
import com.example.client.additional.ControllerExtension;
import com.example.client.ui.cells.WrapCell;
import com.example.client.util.Helper;
import com.example.client.util.SystemClock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.File;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PromotionsController implements ControllerExtension {

    public AnchorPane mainPane;
    @FXML
    private CheckBox amountBox;
    @FXML
    private Menu categoryMenu;

    @FXML
    private Menu discountMenu;
    @FXML
    private Menu monthMenu;

    @FXML
    private Label currentDate;

    @FXML
    private Label goodsAmount;

    @FXML
    private MenuBar menuBar;

    @FXML
    private CheckBox priceBox;

    @FXML
    private ToggleGroup discountGroup;
    @FXML
    private TableView<Product> tableProducts;

    @FXML
    private TableView<Product> tableSellers;

    @FXML
    private ImageView toDefaultProducts;

    @FXML
    private ImageView toDefaultSold;

    private int currDiscount = 4;
    private ArrayList<Category> chosenCategories = new ArrayList<>();
    private ArrayList<Month> chosenMonths = new ArrayList<>();
    AnchorPane pane;
    boolean isDefault = true;

    public void initialize(){

        HelloApplication.setPromotionsController(this);
        setPromotionTableColumns();
        setTableBestSellers();
        configureMenus();
        initializeSortBoxes();
        SystemClock.addDate(currentDate);
        toDefaultProducts.setOnMouseClicked(action -> setToDefaultProducts());
    }
    private void setBestSellersColumns(){
        tableSellers.setPlaceholder(Helper.getNoContentLabel());
        tableSellers.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        TableColumn<Product,String> columnName = new TableColumn<>();
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnName.setCellFactory(tf -> new WrapCell<>());
        columnName.setMinWidth(205);
        tableSellers.getColumns().add(columnName);

        columnName = new TableColumn<>();
        columnName.setCellValueFactory(new PropertyValueFactory<>("fullAmount"));
        columnName.setCellFactory(callback -> new DoubleInfoCell<>(false));
        tableSellers.getColumns().add(columnName);
    }
    private void setTableBestSellers(){
        setBestSellersColumns();
        tableSellers.getSelectionModel().selectedItemProperty().addListener((observableValue, category, t1) -> setPopUpMessage(t1));
    }
    private void setPromotionTableColumns(){
        TableColumn<Product,String> columnName = new TableColumn<>();
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        columnName.setCellFactory(tf -> new WrapCell<>());
        columnName.setMinWidth(160);
        tableProducts.setPlaceholder(Helper.getNoContentLabel());
        tableProducts.getColumns().add(columnName);


        columnName = new TableColumn<>();
        columnName.setCellValueFactory(new PropertyValueFactory<>("priceDiscounted"));
        columnName.setCellFactory(callback -> new DoubleInfoCell<>(true));
        columnName.setMinWidth(80);
        tableProducts.getColumns().add(columnName);

        columnName = new TableColumn<>();
        columnName.setMinWidth(80);
        columnName.setStyle("-fx-text-alignment:right;");
        columnName.setCellValueFactory(new PropertyValueFactory<>("fullDiscount"));

        tableProducts.getColumns().add(columnName);

        columnName = new TableColumn<>();
        columnName.setMinWidth(140);
        columnName.setCellValueFactory(new PropertyValueFactory<>("fullPeriod"));
        columnName.setCellFactory(callback -> new DateCell());
        columnName.setStyle("-fx-translate-x:20px;");
        tableProducts.getColumns().add(columnName);

        TableColumn<Product,Boolean> column = new TableColumn<>();
        column.setCellFactory(callback -> new DotsCell<>());
        column.setMinWidth(115);



        tableProducts.getColumns().add(column);
    }
    private void setToDefaultProducts(){
        chosenMonths.clear();
        chosenCategories.clear();
        currDiscount = 4;

        monthMenu.getItems().forEach(menuItem -> ((CheckMenuItem) menuItem).setSelected(false) );
        categoryMenu.getItems().forEach(menuItem -> ((CheckMenuItem) menuItem).setSelected(false));
        discountGroup.selectToggle(null);

        tableProducts.setItems(FXCollections.observableArrayList(DiscountService.getDiscounts()));
    }
    public void clearData(){
        tableProducts.getItems().clear();
        tableSellers.getItems().clear();

    }
    private void setPopUpMessage(Product product){
        SideBarController controller = HelloApplication.getSideBarController();
        if(controller.getScene().getChildren().contains(pane)) { controller.removePane(pane); }

        pane = new AnchorPane();

        Button[] arr = {new Button("Accept"),new Button("Cancel")};

        pane.getStyleClass().add("custom-pane");

        Text t = new Text("     Do you want to set discount?");
        t.setLayoutY(60);
        t.setLayoutX(50);
        t.setWrappingWidth(300);

        for (Button currButton : arr) {
            currButton.getStyleClass().add("buttonWhite");
            currButton.setTranslateY(110);
            currButton.setMinWidth(140);
            currButton.setLayoutX(currButton.getText().equals("Accept") ? 40 : 200);
            pane.getChildren().add(currButton);
        }
        arr[0].setOnAction(actionEvent -> {
//            HelloApplication.getAddPromotionController().setDiscountFromProduct(product);
            HelloApplication.getSideBarController().removePane(pane);
            HelloApplication.getSideBarController().setPopUp("addPromotionMomento.fxml");
            HelloApplication.getAddPromotionController().setDiscountFromProduct(product);
//            HelloApplication.getSideBarController().addPane(HelloApplication.getAddPromotionController().getFrame());
        });
        arr[1].setOnAction(actionEvent -> HelloApplication.getSideBarController().removePane(pane));

        pane.getChildren().add(t);
        pane.setPrefWidth(380);
        pane.setPrefHeight(200);
        controller.addPane(pane);
        new MessagePopUp(pane);

    }
    public TableView<Product> getDiscountsTable(){
        return tableProducts;
    }
    private void sortProducts(){
        isDefault = false;

        // if the chosen option is full then i filter it
        Stream<Product> productStream = DiscountService.getDiscounts().stream();
        if(chosenCategories.size() > 0) productStream = productStream.filter( product -> chosenCategories.contains(product.getCategory()));
        if (chosenMonths.size() > 0) productStream = productStream.filter(product -> chosenMonths.contains( product.getDiscount().getEndDate().getMonth() ));

        switch (currDiscount){
            case 0 -> productStream = productStream.filter(product -> product.getDiscount().getDiscountProperty() > 0.7);
            case 1 -> productStream = productStream.filter( product -> 0.3 < product.getDiscount().getDiscountProperty() && product.getDiscount().getDiscountProperty() < 0.7);
            case 2 -> productStream = productStream.filter( product -> product.getDiscount().getDiscountProperty() <= 0.3);

        }
        tableProducts.setItems(FXCollections.observableArrayList(productStream.toList()));

        // responsible for rendering
        tableProducts.refresh();
    }
    private void sortProductsByDiscount(String value){
        // 0 - < 30%  ; 1 -  30% < product < 70% ; > 70% ; 4 - all
        switch (value) {
            case "< 30%" -> currDiscount = 0;
            case "30 % < and < 70 %" -> currDiscount = 1;
            case "> 70%" -> currDiscount = 2;
            default -> currDiscount = 4;
        }
        sortProducts();
    }
    private void sortProductsByCategory(Category category){
        if(chosenCategories.contains(category)){chosenCategories.remove(category);}
        else{chosenCategories.add(category);}
        sortProducts();
    }
    private void sortProductsByMonth(Month month){
        if(chosenMonths.contains(month)){chosenMonths.remove(month);}
        else{chosenMonths.add(month);}
        sortProducts();
    }
    public void removeDiscountFromTable(Product product ){
        tableProducts.getItems().remove(product);
        goodsAmount.setText(String.valueOf(DiscountService.getSize()));
        refreshTable();
    }
    void sortBySearch(String name){
        if(!isDefault) setToDefaultProducts();
        Stream<Product> productStream = DiscountService.getDiscounts().stream();
        List<Product> sorted = productStream.filter( product -> product.getName().contains(name)).collect(Collectors.toList());
        tableProducts.setItems(FXCollections.observableArrayList(sorted));

        tableProducts.refresh();
    }

    private void configureMenus(){
        // setting arrows for menus

        // CHANGE AS IN STORAGE
        File f = new File("src/main/resources/com/example/icons/storage/arrow.png");
        Image image  = new Image(f.toURI().toString());
        String[] localArray = {"Discounts","Months","Categories"};

        int i = 0;
        for(Menu currMenu : menuBar.getMenus()){
            ImageView arrow = new ImageView(image);

            arrow.setOpacity(0.5);
            arrow.setScaleX(0.6);
            arrow.setScaleY(0.6);

            Label lab = new Label(localArray[i++]);
            lab.setGraphic(arrow);
            lab.setContentDisplay(ContentDisplay.RIGHT);

            currMenu.setOnShown(shown -> arrow.setRotate(180));
            currMenu.setOnHidden(hidden -> arrow.setRotate(0));
            currMenu.setGraphic(lab);
        }
        // connecting each button to function
        discountMenu.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> sortProductsByDiscount(menuItem.getText())));
        monthMenu.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> sortProductsByMonth(Month.valueOf(menuItem.getText().toUpperCase()))));
        categoryMenu.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> sortProductsByCategory(Category.valueOf(menuItem.getText().toUpperCase()))));

    }
    public void addDiscountToTable(Product product){
        tableProducts.getItems().add(product);
        tableProducts.refresh();
        goodsAmount.setText(String.valueOf(DiscountService.getSize()));
    }

    private void initializeSortBoxes(){
        // sets the logic of work for checkboxes which sort products in tables
        amountBox.selectedProperty().addListener(action -> {
            List<Product> sortedProducts = tableSellers.getItems().stream().sorted(Comparator.comparing(Product::getAmount)).collect(Collectors.toList());

            if(amountBox.isSelected()) Collections.reverse(sortedProducts);
            tableSellers.setItems(FXCollections.observableArrayList(sortedProducts));
        });
        priceBox.selectedProperty().addListener(action -> {
            List<Product> sortedProducts = tableProducts.getItems().stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());

            if(priceBox.isSelected()) Collections.reverse(sortedProducts);
            tableProducts.setItems(FXCollections.observableArrayList(sortedProducts));
        });
    }
    public void refreshTable(){
        tableProducts.refresh();
    }

    @Override
    public AnchorPane getFrame() {
        return mainPane;
    }

    public void setDiscountData(List<Product> discounts) {
        System.out.println("setting data " + discounts.size());
        tableProducts.setItems(FXCollections.observableArrayList(discounts));
        goodsAmount.setText(String.valueOf(discounts.size()));

        ObservableList<Product> newList = FXCollections.observableArrayList(StorageService.getMainProducts()
                .stream()
                .sorted(Comparator.comparing(Product::getAmount))
                .skip(StorageService.getSize()/2)
                .collect(Collectors.toList()));
        tableSellers.setItems(newList);

        tableSellers.refresh();
        tableProducts.refresh();
    }
}
