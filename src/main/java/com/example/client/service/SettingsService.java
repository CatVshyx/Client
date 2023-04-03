package com.example.client.service;

import com.example.client.additional.Response;
import java.io.IOException;
import java.util.HashMap;

public class SettingsService {
    public static Response changeUserCredentials(HashMap<String,String> map) {
        try {
            return HttpClientService.changeUserCredentials(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Response changePassword(String currPassword, String newPassword){
        try {
            return  HttpClientService.changePassword(currPassword,newPassword);
        } catch (IOException e) {
            return new Response("Please be ensure you wrote the correct password",403);
        }
    }
    public static Response uploadPhoto(String path){
        try {
            return  HttpClientService.uploadNewPhoto(path);
        } catch (IOException e) {
            return new Response("Something went wrong",403);
        }
    }

    public static Response leaveCompany() {
        try{
            Session.clearSession();
            return HttpClientService.leaveCompany();
        }catch (IOException e){
            return new Response("Something went wrong",403);
        }
    }
}
