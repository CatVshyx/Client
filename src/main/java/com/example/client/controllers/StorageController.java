package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.model.Product;
import com.example.client.service.StorageService;
import com.example.client.ui.cells.DotsCell;
import com.example.client.ui.cells.DoubleInfoCell;
import com.example.client.additional.Category;
import com.example.client.additional.ControllerExtension;
import com.example.client.ui.cells.WrapCell;
import com.example.client.util.Helper;
import com.example.client.util.SystemClock;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StorageController implements ControllerExtension{

    public AnchorPane mainFrame;
    @FXML
    private MenuBar menuBar;
    @FXML
    private CheckBox amountBox;
    @FXML
    private CheckBox priceBox;
    @FXML
    private TableView<Product> tableBestSellers;
    @FXML
    private TableColumn<Product,String> productName;
    @FXML
    private TableColumn<Product,String> sold;

    @FXML
    private TableView<Product> tableProducts;

    @FXML
    private TableColumn<Product,String> product;

    @FXML
    private TableColumn<Product,String> price;

    @FXML
    private TableColumn<Product,String> amount;

    @FXML
    private TableColumn<Product,String> supplier;
    @FXML
    private Menu categoryMenu;

    @FXML
    private Menu stockMenu;

    @FXML
    private Menu supplierMenu;

    ObservableList<Product> products = FXCollections.observableArrayList();
    @FXML
    private Label currentDate;
    @FXML
    private ToggleGroup stockGroup;
    @FXML
    private Label goodsAmount;
    private int currStock = 0;
    private final ArrayList<Category> chosenCategories = new ArrayList<>();
    private final ArrayList<String> chosenSuppliers = new ArrayList<>();
    @FXML
    private ImageView toDefaultProducts;
    boolean isDefault = true;
    private SimpleStringProperty amountProperty = new SimpleStringProperty("0");
    public void initialize() {
        SystemClock.addDate(currentDate);

        HelloApplication.setStorageController(this);

        toDefaultProducts.setOnMouseClicked( mouseEvent -> setToDefaultProducts() );
        configureTableColumns();
        configureStorageMenus();

        initializeSortBoxes(priceBox,tableProducts);
        initializeSortBoxes(amountBox,tableBestSellers);

//        goodsAmount.setText(String.valueOf(StorageService.getSize()));
        goodsAmount.textProperty().bind(amountProperty);
    }
    private void configureTableColumns(){
        // BEST SELLERS
        tableBestSellers.setPlaceholder(Helper.getNoContentLabel());

        tableBestSellers.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productName.setCellFactory(callback -> new WrapCell<>());

        sold.setCellValueFactory(new PropertyValueFactory<>("fullAmount"));
        sold.setCellFactory(callback -> new DoubleInfoCell<>(false));
        // TABLE PRODUCTS
        tableProducts.setPlaceholder(Helper.getNoContentLabel());
        tableProducts.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        product.setCellValueFactory(new PropertyValueFactory<>("name"));
        product.setCellFactory(callback -> new WrapCell<>());

        price.setCellValueFactory(new PropertyValueFactory<>("fullPrice"));
        price.setCellFactory(callback -> new DoubleInfoCell<>(false));
        amount.setCellValueFactory(new PropertyValueFactory<>("fullAmount"));
        amount.setCellFactory(callback -> new DoubleInfoCell<>(false));
        supplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        tableProducts.setItems(products);

        TableColumn<Product,Boolean> col3 = new TableColumn<>();
        col3.setCellFactory(callback -> new DotsCell<>());
        col3.setMinWidth(115);
        col3.setStyle("-fx-translate-x:-20px");

        tableProducts.getColumns().add(col3);

        tableProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            System.out.println(newSelection);
        });
    }
    private void initializeSortBoxes(CheckBox box, TableView<Product> localTable  ){
        // sets the logic of work for checkboxes which sort products in tables
        Comparator<Product> currComp;

        if(box.equals(priceBox)){ currComp = Comparator.comparing(Product::getPrice); }
        else if(box.equals(amountBox)){ currComp = Comparator.comparing(Product::getAmount); }
        else{ return; }

        box.selectedProperty().addListener(action -> {
            List<Product> sortedProducts = localTable.getItems().stream().sorted(currComp).collect(Collectors.toList());
            if(box.isSelected()) Collections.reverse(sortedProducts);

            localTable.setItems(FXCollections.observableArrayList(sortedProducts));
        });

    }
    void sortBySearch(String name){
        if (!isDefault) setToDefaultProducts();
        try{
            Stream<Product> productStream =products.stream();
            List<Product> sorted = productStream.filter( product -> product.getName().contains(name)).collect(Collectors.toList());
            tableProducts.setItems(FXCollections.observableArrayList(sorted));
        }catch (NullPointerException ignored){}


        tableProducts.refresh();
    }
    private void configureStorageMenus(){
        // sets the context menu logic and design
        setStyleOnMenus(menuBar.getMenus());
        // connecting each checkbox/radiobox to methods
        categoryMenu.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> setProductByCategory(Category.valueOf(menuItem.getText().toUpperCase()))));
        stockMenu.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> setProductByStock(menuItem.getText())));
        categoryMenu.getItems().forEach(menuItem -> menuItem.getStyleClass().add("customCheckbox-table"));

    }
    private void setStyleOnMenus(ObservableList<Menu> menus){
        File f = new File("src/main/resources/com/example/icons/storage/arrow.png");
        Image image  = new Image(f.toURI().toString());

        for (Menu currMenu : menus){
            ImageView arrow = new ImageView(image);
            arrow.setOpacity(0.5);
            arrow.setScaleX(0.6);
            arrow.setScaleY(0.6);

            Label lab = new Label(currMenu.getText());
            lab.setGraphic(arrow);
            lab.setContentDisplay(ContentDisplay.RIGHT);

            currMenu.setText(null);
            currMenu.setOnShown(shown -> arrow.setRotate(180));
            currMenu.setOnHidden(hidden -> arrow.setRotate(0));
            currMenu.setGraphic(lab);
        }
    }
    private void setToDefaultProducts(){
        isDefault = true;

        chosenSuppliers.clear();
        chosenCategories.clear();
        stockGroup.selectToggle(null);

        categoryMenu.getItems().forEach(menuItem -> {
            if(menuItem instanceof CheckMenuItem check){
                check.setSelected(false);
            }
        });
        supplierMenu.getItems().forEach(menuItem -> {
            if(menuItem instanceof CheckMenuItem check){
                check.setSelected(false);
            }
        });
        tableProducts.setItems(products);
    }
    private void sortProducts(){
        // if the chosen option is full then i filter it
        isDefault = false;

        Stream<Product> productStream =products.stream();
        if(chosenCategories.size() > 0) productStream = productStream.filter( product -> chosenCategories.contains(product.getCategory()));
        if (chosenSuppliers.size() > 0) productStream = productStream.filter(product ->  chosenSuppliers.contains(product.getSupplier()));
        List<Product> sorted = productStream.toList();

        // 0 - All in stock    1 -In stock    2 - Out of stock
        switch (currStock) {
            case 0 -> tableProducts.setItems(FXCollections.observableArrayList(sorted));
            case 1 -> tableProducts.setItems(new FilteredList<>(FXCollections.observableArrayList(sorted), product -> product.getAmount() > 0));
            case 2 -> tableProducts.setItems(new FilteredList<>(FXCollections.observableArrayList(sorted), product -> product.getAmount() == 0));
        }
        tableProducts.refresh();
    }

    private void setProductByCategory(Category category){
        // true - means to add this property(category) to the full sorting, false - otherwise
        if(chosenCategories.contains(category)){chosenCategories.remove(category);}
        else{chosenCategories.add(category);}
        sortProducts();
    }
    private void setProductBySupplier(String supplier){
        if(chosenSuppliers.contains(supplier)){ chosenSuppliers.remove(supplier);}
        else{ chosenSuppliers.add(supplier); }
        sortProducts();
    }
    private void setProductByStock(String name){
        currStock = name.equals("All in stock")
                ? 0
                : name.equals("In stock") ? 1 : 2;
        sortProducts();
    }
    public void clearStorageData(){
        tableProducts.getItems().clear();
        tableBestSellers.getItems().clear();

    }
    public void setStorageData(List<Product> args){
        System.out.println("setting data " + args.size());
        products.setAll(args);
        tableProducts.setItems(products);
//        goodsAmount.setText(String.valueOf(args.size()));
        amountProperty.set(String.valueOf(args.size()));
        ObservableList<Product> newList = FXCollections.observableArrayList(StorageService.getMainProducts()
                .stream()
                .sorted(Comparator.comparing(Product::getAmount))
                .skip(StorageService.getSize()/2)
                .collect(Collectors.toList()));
        tableBestSellers.setItems(newList);
        tableProducts.refresh();
        tableBestSellers.refresh();
        setSuppliers();
    }
    private void setSuppliers(){
        ArrayList<String> names = new ArrayList<>();
        supplierMenu.getItems().clear();
        products.forEach(product1 -> {
            if( !names.contains(product1.getSupplier()) )
                names.add(product1.getSupplier());}
        );

        names.forEach(s -> supplierMenu.getItems().add(new CheckMenuItem(s)));
        supplierMenu.getItems().forEach(menuItem -> menuItem.setOnAction(actionEvent -> setProductBySupplier(menuItem.getText())));
    }
    public void refreshTable(){
        tableProducts.refresh();
    }
    public void removeProductFromTable(Product product){
        products.remove(product);
        amountProperty.set(String.valueOf(StorageService.getSize()));
//        goodsAmount.setText(String.valueOf(StorageService.getSize()));
        checkSupplier(product.getSupplier(),true);
        tableProducts.refresh();
    }
    public void addProductToTable(Product product){
        products.add(product);
//        goodsAmount.setText(String.valueOf(StorageService.getSize()));
        amountProperty.set(String.valueOf(StorageService.getSize()));
        checkSupplier(product.getSupplier(),false);
        tableProducts.refresh();
    }
    void checkSupplier(String supplier,boolean remove){
        ObservableList<MenuItem> items = supplierMenu.getItems();
        if (!remove){
            MenuItem item = items.stream().filter(menuItem -> menuItem.getText().equals(supplier)).findAny().orElse(null);
            if (item == null){
                CheckMenuItem localMenu = new CheckMenuItem(supplier);
                localMenu.setOnAction(actionEvent -> setProductBySupplier(localMenu.getText()));
                items.add(localMenu);
            }
            return;
        }

        Product last = products.stream().filter(product1 -> product1.getSupplier().equals(supplier)).findAny().orElse(null);
        if (last == null){ items.removeIf(menuItem -> menuItem.getText().equals(supplier)); }

    }
    @Override
    public AnchorPane getFrame() {
        return mainFrame;
    }

}
