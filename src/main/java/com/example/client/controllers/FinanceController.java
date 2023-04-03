package com.example.client.controllers;

import com.example.client.HelloApplication;
import com.example.client.additional.ControllerExtension;
import com.example.client.service.FinanceService;
import com.example.client.util.ConverterFactory;
import com.example.client.model.Date;
import com.example.client.ui.smoothCharts.SmoothedChart;
import com.example.client.model.Currency;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class FinanceController implements ControllerExtension {
    @FXML
    private AnchorPane yearPane;
    @FXML
    private Label currencyDay;
    @FXML
    private Label currencyWeek;
    @FXML
    private Label currencyMonth;
    @FXML
    private Label currencyYear;
    @FXML
    private Label dayLabel;

    @FXML
    private AnchorPane financePane;

    @FXML
    private Label monLabel;

    @FXML
    private Label revDay;

    @FXML
    private Label revMon;

    @FXML
    private Label revWeek;

    @FXML
    private Label revYear;

    @FXML
    private Label weekLabel;

    @FXML
    private ListView<Pane> listMonths;
    @FXML
    private ListView<Pane> listWeeks;
    @FXML
    private ListView<Pane> listYears;
    @FXML
    private AnchorPane monthPane;
    @FXML
    private AnchorPane paneWeek;
    private Date[] dates = FinanceService.getDates();
    @FXML
    private Label yearLabel;
    @FXML
    private TitledPane titledPaneWeek;
    @FXML
    private TitledPane titledPaneYear;
    @FXML
    private TitledPane titledPaneMonth;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab monthTab;
    @FXML
    private ImageView downloadPDF;
    @FXML
    private AnchorPane revenuePane;
    @FXML
    private Accordion accordion;
    @FXML
    private Label generatedLabel;
    private static Currency curr = ConverterFactory.getCurrency("UAH");
    private static final SimpleStringProperty currencyProperty = new SimpleStringProperty(curr.getName());
    private static final SimpleFloatProperty rateProperty = new SimpleFloatProperty(curr.getRate());
    private final SimpleBooleanProperty isGenerated = new SimpleBooleanProperty(true);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d uuuu");
    public void initialize(){
        HelloApplication.setFinanceController(this);
        downloadPDF.setOnMouseClicked(action -> {

            if(generatePDFFile())setTimeline().play();
        });
    }

    private FadeTransition setTimeline(){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(5000),generatedLabel);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);
        return fadeTransition;
    }
    private void configureInformationPanes(){
        Arrays.asList(currencyDay,currencyMonth,currencyWeek,currencyYear).forEach(curr -> curr.textProperty().bind(currencyProperty));
        if (dates == null || dates.length == 0) return;
        LocalDate currDate = LocalDate.now();
        Stream<Date> stream = Arrays.stream(dates);
        Date now = stream.filter(date1 -> date1.getDate().compareTo(currDate) == 0).findAny().orElse(dates[(dates.length - 1)]);
        float currAmount = 0;
        setInfoOnBounded(revDay,String.valueOf(now.getSold()));
        dayLabel.setText(now.getDate().format(formatter));


        LocalDate ld;
        stream = Arrays.stream(dates);
        Date[] week = stream
                .dropWhile(date -> date.getDate().compareTo(currDate.minusDays(7)) != 0)
                .takeWhile(date -> date.getDate().compareTo(currDate) < 0)
                .toArray(Date[]::new);
        for(int i = 0; i < week.length; i++){
            currAmount += week[i].getSold();
            if(i == week.length - 1){
//                revWeek.setText(String.valueOf(currAmount));
                weekLabel.setText(week[0].getDate().format(formatter) + '-' + week[week.length - 1].getDate().format(formatter) );
            }
        }
        setInfoOnBounded(revWeek,String.valueOf(currAmount));
        currAmount = 0;

        // month
        monLabel.setText(currDate.getMonth().name() +' '+ currDate.getYear());
        for(Date localDate : dates){
            ld = localDate.getDate();
            if(ld.getMonth() != currDate.getMonth() || ld.getYear() != currDate.getYear())
                continue;
            currAmount += localDate.getSold();
        }
        setInfoOnBounded(revMon,String.valueOf(currAmount));
        currAmount = 0;
        //year
        Month beginMonth = Month.DECEMBER;
        Month endMonth = Month.JANUARY;

        for (Date date : dates) {
            ld = date.getDate();
            if (ld.getYear() != currDate.getYear()) continue;
            if (ld.getMonth().getValue() < beginMonth.getValue()) beginMonth = ld.getMonth();
            if (ld.getMonth().getValue() > endMonth.getValue()) endMonth = ld.getMonth();
            currAmount += date.getSold();
        }

        setInfoOnBounded(revYear,String.valueOf(currAmount));
//        revY

        yearLabel.setText(beginMonth.name() + ' ' +
                currDate.getYear() + '-' +
                endMonth.name() + (' ' + currDate.getYear())
        );

    }
    private void setInfoOnBounded(Label label,String info){
        if (label.textProperty().isBound()){label.textProperty().unbind();}
        label.setText(info);
        label.textProperty().bind(rateProperty.multiply(Float.parseFloat(label.getText())).asString());
    }
    private void setChart(AnchorPane chartPane, ListView<Pane> listView, LinkedHashMap<String,int[]> map){

        if (map == null ) return;
        System.out.println("CHART GENERATION   " + map);
        System.out.println("chart size "+map.size());
        int seriesLength = map.values().stream().findFirst().get().length;

        SmoothedChart<Number, Number> localChart = getChartInstance(seriesLength == 30 ? seriesLength + 1 : seriesLength);
        XYChart.Series<Number, Number> series;
        int[] currArr;
        for(String key: map.keySet()){
            series = new XYChart.Series<>();
            currArr = map.get(key);
            for(int i = 0; i < currArr.length; i++){
                series.getData().add(new XYChart.Data<>(i+1,currArr[i]));
            }
            System.out.println("Arrays of added data "+Arrays.toString(currArr));
            Color currColor = Color.color(Math.random(),Math.random(),Math.random());
            localChart.getData().add(series);
            localChart.setSeriesColor(series,currColor);
            Pane pane = setInformation(key,convertToRGBA(currColor), localChart.getData().get(localChart.getData().indexOf(series)).getNode());
            System.out.println("pane created " + pane.getId());
            System.out.println(listView.getItems().add(pane));
        }
        chartPane.getChildren().add(localChart);

    }
    private SmoothedChart<Number,Number> getChartInstance(int max){
        // creates a new instance of SmoothedChart and configures it
        SmoothedChart<Number, Number> chart = new SmoothedChart<>(new NumberAxis(), new NumberAxis());

        chart.setSmoothed(true);
        chart.setChartType(SmoothedChart.ChartType.LINE);
        chart.setInteractive(false);
        chart.setSubDivisions(8);
        chart.setSnapToTicks(false);
        chart.setLegendVisible(false);
        chart.setMinWidth(640);
        chart.setMaxHeight(350);

        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        NumberAxis yAxis = (NumberAxis) chart.getYAxis();

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(max);
        xAxis.setTickUnit(1);

        xAxis.setMinorTickVisible(false);
        yAxis.setMinorTickVisible(false);
        return chart;
    }
    private String convertToRGBA(Color color){
        return "rgba(" +
                (int) (color.getRed() * 255) + ',' +
                (int) (color.getGreen() * 255) + ',' +
                (int) (color.getBlue() * 255) + ',' +
                color.getOpacity() + ')';
    }
    private Pane setInformation(String information, String color, Node series){
        BorderPane p = new BorderPane();
        Text text = new Text(information);

        File f = new File("src/main/resources/com/example/icons/opened_icon.png");
        ImageView imageView = new ImageView(new Image(f.toURI().toString()));
        BorderPane imagePane = new BorderPane();
        imagePane.setCenter(imageView);
        imagePane.setOnMouseClicked(action ->{
            File imageFile;
            if(series.isVisible()){
                imageFile = new File("src/main/resources/com/example/icons/closed_icon.png");
                series.setVisible(false);
                imageView.setImage(new Image(imageFile.toURI().toString()));
            }
            else{
                imageFile = new File("src/main/resources/com/example/icons/opened_icon.png");
                series.setVisible(true);
                imageView.setImage(new Image(imageFile.toURI().toString()));
            }
        });
        imageView.visibleProperty().bindBidirectional(isGenerated);
        imageView.visibleProperty().addListener(((observableValue, oldValue, newValue) -> imageView.setVisible(newValue)));
        imageView.setFitWidth(23);
        imageView.setFitHeight(13);

        p.setLeft(setColorPane(color));
        p.setCenter(text);
        p.setRight(imagePane);

        return p;
    }
    private Pane setColorPane(String color){
        Region regionColor = new Region();
        Pane  colorPane = new Pane();

        colorPane.setStyle("-fx-min-width:60px;  -fx-border-color:rgba(113, 115, 177, 0.5); -fx-border-width: 0 1 0 0;");
        regionColor.setStyle("-fx-background-color:" + color + "; -fx-min-width:15px; -fx-min-height:15px; -fx-background-radius:5px; -fx-translate-x:20px;" +
                " -fx-layout-y:0px; -fx-border-color:" + color.replace("1.0","0.1")
                + "; -fx-border-width:2px; -fx-border-radius:15px;");

        colorPane.getChildren().add(regionColor);
        return colorPane;
    }
    private static byte[] makeAnImageAsByte(Node node){
        BufferedImage buf = SwingFXUtils.fromFXImage(node.snapshot(new SnapshotParameters(), null), null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(buf,"png",baos);
            baos.close();
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean generatePDFFile() {
        // TITLED PANE - IS THE PANE WITH MY WEEKS AND pane - there are my generated charts
        if (dates == null || dates.length == 0) return false;
        isGenerated.setValue(false);
        HashMap<TitledPane,AnchorPane> mapOfCharts = new LinkedHashMap<>(Map.of(
                titledPaneWeek,paneWeek,
                titledPaneMonth,monthPane,
                titledPaneYear,yearPane));

        FinanceService.generateFile(convertImagesToBytes(mapOfCharts),
                currencyProperty.get(),
                rateProperty.get());
        Tab previous = tabPane.getSelectionModel().getSelectedItem();

        tabPane.getSelectionModel().select(previous);
        accordion.setExpandedPane(null);
        isGenerated.setValue(true);
        return true;
    }
    private LinkedHashMap<String, byte[][]> convertImagesToBytes(HashMap<TitledPane,AnchorPane> mapOfCharts){
        LinkedHashMap<String, byte[][]> convertedCharts = new LinkedHashMap<>();
        mapOfCharts.forEach((key,value) -> {
            tabPane.getSelectionModel().select(monthTab);
            key.setExpanded(true);
            ArrayList<byte[]> images = new ArrayList<>();
            ObservableList<XYChart.Series<Number,Number>> objs = ((SmoothedChart<Number, Number>) value.getChildren().get(0)).getData();
            objs.forEach(series -> series.getNode().setVisible(false));
            for(int i = 0; i < objs.size(); i++){
                objs.get(i).getNode().setVisible(true);
                if(i%3 == 0 && i != 0 || objs.size() - i == 1){
                    // divide them by 3
                    byte[] local = makeAnImageAsByte(value);
                    objs.forEach(series -> series.getNode().setVisible(false));
                    images.add(local);
                }
            }
            //when i made all the images of the chart , then in the end I add titled pane(key)

            images.add(makeAnImageAsByte(key));
            objs.forEach(series -> series.getNode().setVisible(true));
            // as the output i`ve hot the name(title) and array of images as bytes
            convertedCharts.put(key.getText(),images.toArray(new byte[0][0]));
        });
        return convertedCharts;
    }

    public void clearData(){
        dates = null;
        Platform.runLater(() -> {
        listWeeks.getItems().clear();
        listYears.getItems().clear();
        listMonths.getItems().clear();


            paneWeek.getChildren().clear();
            monthPane.getChildren().clear();
            yearPane.getChildren().clear();
        });

    }
    public void setData(Date[] data){
        clearData();
        dates = data;
        Platform.runLater(() -> {
            setChart(paneWeek,listWeeks,FinanceService.getSpecifiedCountedData(0));
            setChart(monthPane,listMonths,FinanceService.getSpecifiedCountedData(1));
            setChart(yearPane,listYears,FinanceService.getSpecifiedCountedData(2));
        });

        configureInformationPanes();
    }

    public static void setCurrentCurrency(Currency currency){
        curr = currency;
        currencyProperty.set(currency.getName());
        rateProperty.set(currency.getRate());
    }
    @Override
    public AnchorPane getFrame() {
        return financePane;
    }
}