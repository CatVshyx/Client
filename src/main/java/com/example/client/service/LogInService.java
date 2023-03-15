package com.example.client.service;

import com.example.client.HelloApplication;
import com.example.client.additional.Response;
import com.example.client.model.RegistrationRequest;
import java.io.IOException;


public final class LogInService {
    public static Response login(String email, String password){
        try {
            Response response =  HttpClientService.login(email,password);
            if (response.getHttpCode() == 200){
                // if login part is successful - creates session - there it creates a thread
                Runnable runnable = HttpClientService::setSessionWithServer;
                Thread thread = new Thread(runnable,"request");
                thread.start();
            }
            return HttpClientService.login(email,password);
        }catch (IOException e){
            return new Response("You don`t have permissions",403);
        }

    }
    public static Response joinCompany(String invitationCode){
        try {
            return HttpClientService.joinByCode(invitationCode);
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }
    }
    public static Response register(RegistrationRequest request){
        try {
             return HttpClientService.register(request);
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }


    }
    public static void logOut(){
        Session.clearSession();
        Thread local = Thread.getAllStackTraces().keySet().stream()
                .filter(thread -> thread.getName().equals("session"))
                .findAny()
                .orElse(null);
        if (local != null && local.isAlive()){
            local.interrupt();
        }
        HttpClientService.logout();
        HelloApplication.loadLoginFrame();

    }
    public static Response createCompany(String name){
        try {
            return HttpClientService.createCompany(name);
        } catch (IOException e) {
            return new Response("You don`t have permissions",403);
        }
    }

    public static Response forgotPassword(String email) {
        try {
            return HttpClientService.forgotPassword(email);
        } catch (IOException e) {
            return new Response("Something went wrong",403);
        }
    }

    public static Response resetPasswordByCode(String code) {
        try {
            return HttpClientService.sendCode(code);
        } catch (IOException e) {
            return new Response("Something went wrong",403);
        }
    }
}
