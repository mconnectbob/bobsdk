package com.bob.bobapp.api.request_object;

public class MFOrderValidationRequest extends GlobalRequestObject{

    private static GlobalRequestObject globalRequestObject = null;

    public static GlobalRequestObject getGlobalRequestObject() {

        if (globalRequestObject == null) {

            globalRequestObject = new GlobalRequestObject();
        }

        return globalRequestObject;
    }

    public static void createGlobalRequestObject(GlobalRequestObject globalRequestObjectLocal) {

        globalRequestObject = globalRequestObjectLocal;
    }
}