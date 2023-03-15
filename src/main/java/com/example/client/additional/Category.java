package com.example.client.additional;

public enum Category {
    DAIRY,FRUIT,VEGETABLES,CEREAL,MEAT,FISH,GRAINS,SWEET,WATER,SNACKS;
    public static boolean checkCategory(String category){
        try{
            Category.valueOf(category.toUpperCase());
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
