package com.example.client.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.time.LocalDate;

public class Helper {
    public static boolean isNumeric(String text){
        try{
            Integer.parseInt(text);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    public static boolean isFloat(String text){
        try{
            Float.parseFloat(text);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    public static boolean isDateGreater(LocalDate localFrom, LocalDate localTo){
        if(localFrom == null || localTo == null) return false;
        return localFrom.compareTo(localTo) > 0;
    }
    public static boolean isFilled(TextField[] fields){
        boolean flag = true;

        String mainStyle = fields[0].getStyle();
        for (TextField field : fields){
            if(field.getText().length() < 2) {
                field.setStyle(mainStyle.replace("rgba(113, 115, 177, 0.2)","#D2042D"));
                flag = false;
            }else{
                field.setStyle(mainStyle.replace("#D2042D","rgba(113, 115, 177, 0.2)"));
            }
        }
        return flag;
    }
    public static void setPictureOnImage(File f, Circle circle){
        if (f == null){
            f = new File("src/main/resources/com/example/icons/default_user.png");
            System.out.println(f.exists() + "exists?");
        }
        Image image = new Image(f.toURI().toString(),300,300,false,true);
        circle.setFill(new ImagePattern(image));
    }
    public static VBox getNoContentLabel(){
        Label label =  new Label("No content available");
        label.setStyle("-fx-font-size:15px;");
        VBox noContent = new VBox();
        File f = new File("src/main/resources/com/example/icons/no-content.png");
        Image image = new Image(f.toURI().toString(),125,75,false,true);
        noContent.getChildren().addAll(label,new ImageView(image));
        noContent.setOpacity(0.5);
        noContent.setSpacing(20);
        noContent.setAlignment(Pos.CENTER);

        return noContent;
    }
}
