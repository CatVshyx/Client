package com.example.client.service;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.Discount;
import com.example.client.model.Product;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscountService {

    private final static List<Product> discounts = new ArrayList<>(
    );
    private DiscountService(){  }

    public static Response addDiscount(Product request){

        try {
            JSONObject object = new JSONObject();
            Discount discount = request.getDiscount();
            object.put("id",request.getId())
                    .put("discount",discount.getDiscountProperty())
                    .put("start_date",discount.getStartDate())
                    .put("end_date",discount.getEndDate());
            Response response = HttpClientService.addDiscount(object.toString());
            if (response.getHttpCode() == 200) {
                discounts.add(request);
                HelloApplication.getPromotionsController().addDiscountToTable(request);
            }
            return response;
        } catch (IOException e) {
            return new Response("You are not allowed to add new discounts",403);
        }
    }
    public static List<Product> getDiscounts(){
        return discounts;
    }
    public static long getSize(){
        return discounts.size();
    }

    public static Response editDiscount(Product product,Discount discount) {
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",product.getId());
            jsonObject.put("start_date",discount.getStartDate());
            jsonObject.put("discount",discount.getDiscountProperty());
            jsonObject.put("end_date",discount.getStartDate());
            Response response = HttpClientService.editDiscount(jsonObject.toString());
            if (response.getHttpCode() == 200){
                product.setDiscount(discount);
                HelloApplication.getPromotionsController().refreshTable();
            }
            return response;
        }catch (IOException e){
            return new Response("You don`t have permissions",403);
        }
    }
    public static void setData(Set<Product> data){
        if (data == null) return;
        clearDiscountData();
        List<Product> filtered = data.stream().filter(product -> product.getDiscount() != null).collect(Collectors.toList());
        discounts.addAll(filtered);
        HelloApplication.getPromotionsController().setDiscountData(discounts);
    }
    public static void clearDiscountData(){
        discounts.clear();
        HelloApplication.getPromotionsController().clearData();
    }
    public static Response deleteDiscount(Product currProduct) {
        try{
            JSONObject object = new JSONObject();
            object.put("id",currProduct.getId());
            Response response = HttpClientService.deleteDiscount(object.toString());
            if (response.getHttpCode() == 200){
                discounts.remove(currProduct);
                HelloApplication.getPromotionsController().removeDiscountFromTable(currProduct);
            }
            return response;
        }catch (IOException e){
            return new Response("You don`t have permissions",403);
        }

    }
}
