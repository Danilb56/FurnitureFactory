package com.example.furniture_factory.utils;

import com.example.furniture_factory.services.FurnitureService;

public class IdUtils {
    private static Long lastFurnitureId = -1L;

    public static Long getNewFurnitureId(FurnitureService furnitureService) {
        if (lastFurnitureId.equals(-1L)) {
            lastFurnitureId = furnitureService.getLargestId();
        }
        lastFurnitureId++;
        return lastFurnitureId;
    }
}
