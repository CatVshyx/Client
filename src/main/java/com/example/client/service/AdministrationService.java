package com.example.client.service;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public  class AdministrationService {
    private static final List<User> users = new ArrayList<>();
    public static Response deleteUser(User user){
        try {
            Response response = HttpClientService.deleteUser(user);
            if (response.getHttpCode() == 200){
                users.remove(user);
                HelloApplication.getAdministrationController().removeUserFromTable(user);
                HelloApplication.getPromotionsController().refreshTable();
            }
            return response;
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }
    }
    public static void clearAdministrationData(){
        users.clear();
        HelloApplication.getAdministrationController().clearData();
    }
    public static Collection<User> getAllUsers(){
        return users;
    }
    public static Response sendInvitation(String request) {
        try {
            return HttpClientService.sendInvitation(request);
        } catch (IOException e) {
            return new Response("You don`t have allows to invite",403);
        }
    }
    public static Response changeAllows(String request){
        try {
            Response response = HttpClientService.changeAllows(request);
            if (response.getHttpCode() == 200){
                HelloApplication.getPromotionsController().refreshTable();
            }
            return response;
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }
    }
    public static InputStream getUserPhoto(String id){
        try {
            if (id == null){
                File f = new File("src/main/resources/com/example/icons/default_user.png");
                return new FileInputStream(f);
            }
            return HttpClientService.getUserPhoto(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setData(Set<User> added) {
        if(added == null) return;
        clearAdministrationData();
        users.clear();
        users.addAll(added);
        HelloApplication.getAdministrationController().setData(users);
    }

    public static int getSize() {
        return users.size();
    }
}
