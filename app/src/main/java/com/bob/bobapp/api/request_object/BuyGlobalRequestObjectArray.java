package com.bob.bobapp.api.request_object;

import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BuyGlobalRequestObjectArray
{
    @SerializedName("UniqueIdentifier")
    private String uniqueIdentifier;

    @SerializedName("Source")
    private String source;

    public ArrayList<InvestmentCartDetailsResponse> getRequestBodyObjectArrayList() {
        return requestBodyObjectArrayList;
    }

    public void setRequestBodyObjectArrayList(ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList) {
        this.requestBodyObjectArrayList = requestBodyObjectArrayList;
    }

    @SerializedName("RequestBody")
    private ArrayList<InvestmentCartDetailsResponse> requestBodyObjectArrayList;


    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
