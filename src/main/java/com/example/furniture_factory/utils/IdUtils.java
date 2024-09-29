package com.example.furniture_factory.utils;

import com.example.furniture_factory.services.ComponentService;
import com.example.furniture_factory.services.FurnitureLineService;
import com.example.furniture_factory.services.FurnitureService;
import com.example.furniture_factory.services.UserService;

public class IdUtils {
    private static Long lastFurnitureId = -1L;
    private static Long lastFurnitureLineId = -1L;
    private static Long lastComponentId = -1L;
    private static Long lastUserId = -1L;

    public static Long getNewFurnitureId(FurnitureService furnitureService) {
        if (lastFurnitureId.equals(-1L)) {
            lastFurnitureId = furnitureService.getLargestId();
        }
        lastFurnitureId++;
        return lastFurnitureId;
    }

    public static Long getNewFurnitureLineId(FurnitureLineService furnitureLineService) {
        if (lastFurnitureLineId.equals(-1L)) {
            lastFurnitureLineId = furnitureLineService.getLargestId();
        }
        lastFurnitureLineId++;
        return lastFurnitureLineId;
    }

    public static Long getNewComponentId(ComponentService componentService) {
        if (lastComponentId.equals(-1L)) {
            lastComponentId = componentService.getLargestId();
        }
        lastComponentId++;
        return lastComponentId;
    }

    public static Long getNewUserId(UserService userService) {
        if (lastUserId.equals(-1L)) {
            lastUserId = userService.getLargestId();
        }
        lastUserId++;
        return lastUserId;
    }
}
