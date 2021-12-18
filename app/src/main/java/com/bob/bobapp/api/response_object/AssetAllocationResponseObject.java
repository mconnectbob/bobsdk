package com.bob.bobapp.api.response_object;

import android.graphics.drawable.Drawable;

public class AssetAllocationResponseObject {

    private String AssetAllocationType;

    private String ClientCode;
    private String DataSource;

    public String getDataSource() {
        return DataSource;
    }

    public void setDataSource(String dataSource) {
        DataSource = dataSource;
    }

    private String AssetClassCode;

    private String AssetClassName;

    private String ValueAmount;

    private String ValuePercentage;

    private String TargetValuePercentage;

    private Drawable colorDrawable;

    public Drawable getColorDrawable() {
        return colorDrawable;
    }

    public void setColorDrawable(Drawable colorDrawable) {
        this.colorDrawable = colorDrawable;
    }

    public String getAssetAllocationType() {
        return AssetAllocationType;
    }

    public void setAssetAllocationType(String assetAllocationType) {
        AssetAllocationType = assetAllocationType;
    }

    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public String getAssetClassCode() {
        return AssetClassCode;
    }

    public void setAssetClassCode(String assetClassCode) {
        AssetClassCode = assetClassCode;
    }

    public String getAssetClassName() {
        return AssetClassName;
    }

    public void setAssetClassName(String assetClassName) {
        AssetClassName = assetClassName;
    }

    public String getValueAmount() {
        return ValueAmount;
    }

    public void setValueAmount(String valueAmount) {
        ValueAmount = valueAmount;
    }

    public String getValuePercentage() {
        return ValuePercentage;
    }

    public void setValuePercentage(String valuePercentage) {
        ValuePercentage = valuePercentage;
    }

    public String getTargetValuePercentage() {
        return TargetValuePercentage;
    }

    public void setTargetValuePercentage(String targetValuePercentage) {
        TargetValuePercentage = targetValuePercentage;
    }
}
