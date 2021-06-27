package com.myweddi.users;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceUtils {

    public static final Map<ServiceType, String> serviceTypeName = new LinkedHashMap<>();
    static {
        serviceTypeName.put(ServiceType.PHOTOGRAPHER, "Fotograf");
        serviceTypeName.put(ServiceType.MUSIC, "DJ/Zespół muzyczny");
        serviceTypeName.put(ServiceType.DANCE, "Nauka tańca");
        serviceTypeName.put(ServiceType.CAMERA, "Kamerzysta");
        serviceTypeName.put(ServiceType.WEDDINGHOUSE, "Dom weselny");
        serviceTypeName.put(ServiceType.FLOWERS, "Kwiaciarnia/florystka");
        serviceTypeName.put(ServiceType.DECORATOR, "Dekorator(ka)");
        serviceTypeName.put(ServiceType.SHOP, "Sklep");
        serviceTypeName.put(ServiceType.ANIMATOR, "Animator(ka)");
    }

    public static final Map<Integer, String> rangeTypeName = new LinkedHashMap<>();
    static{
        rangeTypeName.put(0, "stacjonarnie");
        rangeTypeName.put(-1, "~ (online)");
        rangeTypeName.put(10, "10 km");
        rangeTypeName.put(20, "20 km");
        rangeTypeName.put(50, "50 km");
        rangeTypeName.put(100, "100 km");
        rangeTypeName.put(200, "200 km");
        rangeTypeName.put(300, "300 km");
        rangeTypeName.put( 301, "> 300 km");
    }
}
