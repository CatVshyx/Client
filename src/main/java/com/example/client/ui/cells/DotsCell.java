package com.example.client.ui.cells;

import com.example.client.HelloApplication;
import com.example.client.model.Product;
import com.example.client.model.User;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import java.io.File;

public class DotsCell<S> extends TableCell<S,Boolean> {
    final Button cellButton = new Button();
    private static final AnchorPane scene = HelloApplication.getSideBarController().getScene();
    public DotsCell() {
        ImageView imageView = new ImageView(new Image(new File("src/main/resources/com/example/icons/storage/dots.png").toURI().toString()));
        imageView.setScaleX(0.6);
        imageView.setScaleY(0.6);
        cellButton.getStyleClass().add("buttonDots");
        cellButton.setGraphic(imageView);

        cellButton.setOnMouseClicked(action -> {
                    S item = this.getTableRow().getItem();
                    if (item == null) return;
                    if (action.getButton() == MouseButton.SECONDARY) {
                        if (item instanceof Product) {
                            HelloApplication.getSideBarController().openSellPane((int) ((Product) item).getId());
                            HelloApplication.getSideBarController().setSceneOpacity(0.3f);
                        }
                    }
                    if (action.getButton() == MouseButton.PRIMARY) {
                        scene.getChildren().forEach(child -> System.out.println(child.getId()));
                        if (this.getTableRow().getTableView() == HelloApplication.getPromotionsController().getDiscountsTable()) {
                            HelloApplication.getSideBarController().setPopUp("promotionPopUp.fxml");
                            HelloApplication.getPromotionPopUpController().setDiscount((Product) item);

//                            Pane pane = HelloApplication.getPromotionPopUpController().getFrame();
//                            System.out.println(pane.getId() + " our");
//                            pane.setVisible(true);
//                            System.out.println(scene.getChildren().add(pane));
                        } else if (item instanceof Product) {
                            HelloApplication.getSideBarController().setPopUp("productPopUp.fxml");
                            HelloApplication.getProductEditController().setProduct((Product) item);
//                            scene.getChildren().add(HelloApplication.getProductEditController().getFrame());
                        } else if (item instanceof User) {
                            HelloApplication.getSideBarController().setPopUp("userPopUp.fxml");
                            HelloApplication.getUserPopUpController().setUser((User) item);

//                            scene.getChildren().add(HelloApplication.getUserPopUpController().getFrame());
                        }

                        HelloApplication.getSideBarController().setSceneOpacity(0.3f);
                    }
                }
        );
    }
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty){
            setGraphic(cellButton);
        }
    }
}
