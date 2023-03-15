package com.example.client.util;

import com.example.client.model.Currency;

import java.util.ArrayList;
import java.util.Arrays;

public class ConverterFactory {

    private static final ArrayList<Currency> currencies = new ArrayList<>(Arrays.asList( new Currency("UAH",1), new Currency("USD",0.027f),new Currency("EUR",0.025f) ));

    public static Currency getCurrency(String name){
        Currency found = currencies.stream().filter(local -> local.getName().equals(name))
                .findAny()
                .orElse(null);

        if(found == null){
            found = new Currency(name,(float) Math.random()*6);
            currencies.add(found);
        }
        return found;
    }
}
