package com.example.client.service;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StorageService {
    // on creation just new arraylist
    private static final List<Product> mainProducts = new ArrayList<>();
    public static Product findProductByName(String name){
        return mainProducts.stream()
                .filter(productTest -> productTest.getName().equals(name))
                .findFirst().orElse(null);
    }
    public static Product findProductById(long id){
        return mainProducts.stream()
                .filter(product -> product.getId() == id)
                .findAny().orElse(null);
    }
    public static void clearData(){
        HelloApplication.getStorageController().clearStorageData();
        mainProducts.clear();
    }
    public static Response addProduct(Product product){
        Response response;
        try {
            response = HttpClientService.addNewProduct(product);
            int code = response.getHttpCode();
            System.out.println(response);
            if (code == 200){
                mainProducts.add(product);
                HelloApplication.getStorageController().addProductToTable(product);
            }
            return response;
        } catch (IOException e) {
            return new Response("You dont have permissions",403);
        }

    }
    public static Response removeProduct(Product product){
        Response response;
        try {
            response = HttpClientService.deleteProduct(product);
            int code = response.getHttpCode();
            System.out.println(response);
            if (code == 200){
                mainProducts.remove(product);
                HelloApplication.getStorageController().removeProductFromTable(product);
            }
            return response;
        } catch (IOException e) {
            return new Response("Not allowed to remove products",403);
        }
    }
    public static Response editProduct(Product product){
        Response response;
        try {
            response = HttpClientService.editProduct(product);
            if (response.getHttpCode() == 200){
                HelloApplication.getStorageController().refreshTable();
            }
            return response;
        } catch (IOException e) {
            return new Response("Not allowed to edit products",403);
        }
    }
    public static List<Product> getMainProducts(){
        return mainProducts;
    }
    public static long getSize(){
        return mainProducts.size();
    }

    public static Response sellProduct(String request){
        try{
            return HttpClientService.sellProduct(request);
        }catch (IOException e){
            return new Response("Something went wrong",403);
        }
    }

    public static void setData(Set<Product> productSet) {
        clearData();
        mainProducts.addAll(productSet);
        HelloApplication.getStorageController().setStorageData(mainProducts);
    }
}
