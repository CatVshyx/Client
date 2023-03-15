package com.example.client.ui.cells;

import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class WrapCell<S> extends TableCell<S,String> {
    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        if(!b){
            Text text = new Text();
            this.setGraphic(text);
            this.setPrefHeight(Control.USE_COMPUTED_SIZE);

            text.wrappingWidthProperty().bind(this.getTableColumn().widthProperty().subtract(5));
            text.textProperty().bind(this.itemProperty());
        }
    }
}
