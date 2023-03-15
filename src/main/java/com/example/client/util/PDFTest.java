package com.example.client.util;

import com.example.client.service.Session;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PDFTest {
    private static  LinkedHashMap<String ,HashMap<String,int[]>> dataTable;
    private static  LinkedHashMap<String, byte[][]> charts;

    public static void setData(LinkedHashMap<String ,HashMap<String,int[]>> data) {
        // String - statistics on this data , int[] - array of data already counted
        PDFTest.dataTable = data;
    }
    public static void setCharts(LinkedHashMap<String, byte[][]> myCharts ){
        // first key of this map - is legend
        // map  - a collection of different images - byte[][] // the last image - is definitely a legend
        charts = myCharts;
    }
    public static void createDocument(String path){
        // to separate the text - add new LineSeparator and then can customize line separator(dotted,dashed,solid etc), width, color
        try {
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);
            document.add(addHeader("Your company \n%s\n  \n time of creation\n%s\n".formatted(Session.getMyCompany().getName(), LocalDate.now())));
            document.add(new Paragraph("\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum").setMarginTop(10));
            document.add(new LineSeparator(new SolidLine(3)));
            document.add(new Paragraph("COLORS MEANING").setMarginTop(10).setFontSize(16));
            document.add(new Paragraph("RED - the price is lower than average\nYELLOW - the price is average\nGREEN - the price is higher than average").setMarginTop(10).setMarginBottom(20));
            document.add(new LineSeparator(new SolidLine(3)));
            document.add(new Paragraph("TABLE INFORMATION").setMarginBottom(10).setFontSize(16));

            setTables(document);
            setCharts(document);

            document.close();


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
    private static void setCharts(Document document){
        if (charts == null) return;
        charts.forEach( (key,images) -> {
            document.add(new LineSeparator(new SolidLine(1)));
            document.add(new Paragraph(key).setMarginTop(15));
            for(int i = 0; i < images.length; i++){
                if(images.length - i == 1){
                    document.add(addImage(images[i],200,170).setMarginTop(20));
                }else {
                    document.add(addImage(images[i], 320, 180));
                }
            }
            document.add(new AreaBreak());

        } );
        charts.clear();
    }
    private static void setTables(Document document){
        // SETS TABLES from map (HashMap<String,int[]> ) and gets statistics from key (String)
        if (dataTable == null) return;
        dataTable.forEach((key,map) -> {
            int length = map.values().stream().findFirst().get().length;
            AtomicLong max = new AtomicLong();
            AtomicInteger size = new AtomicInteger();
            if (length == 30) length++;
            map.values().forEach(arr -> max.addAndGet(Arrays.stream(arr).sum()));
            map.values().forEach(arr -> size.set((int) (size.get() + Arrays.stream(arr).filter(value -> value != 0).count()) ));
            Div div = new Div()
                    .add(new Paragraph(key))
                    .add(addTable(map,length + 1, max.get()/size.get()))
                    .setKeepTogether(false);
            document.add(div);
        });

        document.add(new Paragraph("Charts information").setMarginTop(10).setFontSize(16));
        document.add(new LineSeparator(new SolidLine(3)));

        dataTable.clear();
    }
    private static Table addHeader(String companyInfo){
        Table t = new Table(new float[]{280f, 150f,80f});
        t.setKeepTogether(true).setBackgroundColor(new DeviceRgb(34,23,44)).setFontColor(new DeviceRgb(255,255,255));

        // i can customize each cell individually
        // i also need to set border for cell, setting border for cell and for the element in the cell is different
        t.addCell(new Cell().add(new Paragraph("Report Information").setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMarginBottom(40f)
                .setFontSize(30f)
                .setBorderBottom(new SolidBorder(new DeviceRgb(255,160,122),3) ))
                .setBorder(Border.NO_BORDER)
        );
        t.addCell(new Cell().add(addImage("C:/Users/Kaiwren/Desktop/report-logo.png",50,50)).setBorder(Border.NO_BORDER) );
        t.addCell(new Cell().add(new Paragraph(companyInfo).setFontSize(10)).setBorder(Border.NO_BORDER) );
        return t;
    }
    private static Table addTable(HashMap<String,int[]> map, int columns, long  average){
        // sets by rows , key - is the first value in the row and the array of ints fills the rest cell
        Table table = new Table(columns);
        //set headers
        for (int i = 0; i < columns; i++){
            Cell c = new Cell();
            c.add(new Paragraph(String.valueOf(i == 0 ? "" : i)).setMarginRight(10));
            table.addCell(c);
        }
        table.setKeepTogether(true);
        table.setMarginTop(15f).setMarginBottom(30f);
        for(String key : map.keySet()){
            table.addCell(new Cell().add(new Paragraph(key)).setBackgroundColor(new DeviceRgb(255,255,255)));
            int[] keyArray = map.get(key);

            for (int value : keyArray) {
                DeviceRgb color = value < average / 2
                        ? new DeviceRgb(250, 128, 114)
                        : value < average
                        ? new DeviceRgb(255, 250, 205)
                        : new DeviceRgb(144, 238, 144);
                Cell cell = new Cell().add(new Paragraph(String.valueOf(value)).setTextAlignment(TextAlignment.CENTER));
                cell.setBackgroundColor(color);
                table.addCell(cell);
            }
            table.startNewRow();
        }
        table.setFontSize(columns > 15 ? 3 : 7);
        return table;
    }
    public static Image addImage(byte[] arr, int width, int height){
        ImageData imageData = ImageDataFactory.create(arr);
        return new Image(imageData).setWidth(width).setHeight(height);
    }
    public static Image addImage(String imagePath, int width, int height){
        ImageData imageData;
        try {
            imageData = ImageDataFactory.create(imagePath);
            return new Image(imageData).setWidth(width).setHeight(height).setMarginTop(20);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
