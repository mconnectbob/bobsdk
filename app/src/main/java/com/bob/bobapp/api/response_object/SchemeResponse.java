package com.bob.bobapp.api.response_object;

import java.io.Serializable;

public class SchemeResponse implements Serializable
{    private String Classification;
    private String MorningStarRating;
    private String Returns6Month;
    private String Returns3Month;
    private String Returns3Year;

    public String getMorningStarRating() {
        return MorningStarRating;
    }

    public void setMorningStarRating(String morningStarRating) {
        MorningStarRating = morningStarRating;
    }

    public String getReturns6Month() {
        return Returns6Month;
    }

    public void setReturns6Month(String returns6Month) {
        Returns6Month = returns6Month;
    }

    public String getReturns3Month() {
        return Returns3Month;
    }

    public void setReturns3Month(String returns3Month) {
        Returns3Month = returns3Month;
    }

    public String getReturns3Year() {
        return Returns3Year;
    }

    public void setReturns3Year(String returns3Year) {
        Returns3Year = returns3Year;
    }

    public String getReturns1Year() {
        return Returns1Year;
    }

    public void setReturns1Year(String returns1Year) {
        Returns1Year = returns1Year;
    }

    public String getReturns5Year() {
        return Returns5Year;
    }

    public void setReturns5Year(String returns5Year) {
        Returns5Year = returns5Year;
    }

    private String Returns1Year;
    private String Returns5Year;



    public String getClassification() {
        return Classification;
    }

    public void setClassification(String classification) {
        Classification = classification;
    }



    private String  TranType;

    private String DividendOption;

    private String ExpiryDate;

    private String OfferLink;

    private String MinimumInvestmentAmount;

    private String IsSwitchAllowed;

    private String MinSWPUnit;

    private String AMCCode;

    private String IsSTPAllowed;

    private String MinSwitchOutAmmount;

    private String IsRedeemAllowed;

    private String IsPurchaseAllowed;

    private String SchemeCode;

    private String CostOfInvestment;

    private String CurrentFundValue;

    private String FolioNumber;

    private String MinSwitchOutAmt;

    private String LotSize;

    private String LatestNAV;

    private String SIPAggrAmount;

    private String MinSipInstallmentAmount;

    private String RecommendationStatus;

    private String FundRiskRating;

    private String SettlementBankCode;

    private String MinSipInstallmentMonth;

    private String SchemeName;

    private String CurrentUnit;

    private String AwaitingHoldingUnit;

    private String Options;

    private String AssetClassCode;

    private String ExistingAmount;

    private String AdditionalPurchaseAmount;

    private String MinRedeemUnit;

    private String ExistingUnits;

    private String ValueResearchRating;

    private String IsSWPAllowed;

    private String IsHasHolding;

    private String Nature;

    private String HasHolding;

    private String AssetClassName;

    private String IsDividend;

    private String RedeemAllowed;

    private String AMCName;

    private String SipStartDates;

    private String MinRedeemAmount;

    private String SwitchOutAllowed;

    private String IsSipAllowed;

    private String ClientCode;

    private String AwaitingHoldingFundValue;

    private String EffectiveDate;

    public String getTranType ()
{
    return TranType;
}

    public void setTranType (String TranType)
    {
        this.TranType = TranType;
    }

    public String getDividendOption ()
{
    return DividendOption;
}

    public void setDividendOption (String DividendOption)
    {
        this.DividendOption = DividendOption;
    }

    public String getExpiryDate ()
    {
        return ExpiryDate;
    }

    public void setExpiryDate (String ExpiryDate)
    {
        this.ExpiryDate = ExpiryDate;
    }

    public String getOfferLink ()
{
    return OfferLink;
}

    public void setOfferLink (String OfferLink)
    {
        this.OfferLink = OfferLink;
    }

    public String getMinimumInvestmentAmount ()
    {
        return MinimumInvestmentAmount;
    }

    public void setMinimumInvestmentAmount (String MinimumInvestmentAmount)
    {
        this.MinimumInvestmentAmount = MinimumInvestmentAmount;
    }

    public String getIsSwitchAllowed ()
    {
        return IsSwitchAllowed;
    }

    public void setIsSwitchAllowed (String IsSwitchAllowed)
    {
        this.IsSwitchAllowed = IsSwitchAllowed;
    }

    public String getMinSWPUnit ()
    {
        return MinSWPUnit;
    }

    public void setMinSWPUnit (String MinSWPUnit)
    {
        this.MinSWPUnit = MinSWPUnit;
    }

    public String getAMCCode ()
{
    return AMCCode;
}

    public void setAMCCode (String AMCCode)
    {
        this.AMCCode = AMCCode;
    }

    public String getIsSTPAllowed ()
    {
        return IsSTPAllowed;
    }

    public void setIsSTPAllowed (String IsSTPAllowed)
    {
        this.IsSTPAllowed = IsSTPAllowed;
    }

    public String getMinSwitchOutAmmount ()
{
    return MinSwitchOutAmmount;
}

    public void setMinSwitchOutAmmount (String MinSwitchOutAmmount)
    {
        this.MinSwitchOutAmmount = MinSwitchOutAmmount;
    }

    public String getIsRedeemAllowed ()
    {
        return IsRedeemAllowed;
    }

    public void setIsRedeemAllowed (String IsRedeemAllowed)
    {
        this.IsRedeemAllowed = IsRedeemAllowed;
    }

    public String getIsPurchaseAllowed ()
    {
        return IsPurchaseAllowed;
    }

    public void setIsPurchaseAllowed (String IsPurchaseAllowed)
    {
        this.IsPurchaseAllowed = IsPurchaseAllowed;
    }

    public String getSchemeCode ()
    {
        return SchemeCode;
    }

    public void setSchemeCode (String SchemeCode)
    {
        this.SchemeCode = SchemeCode;
    }

    public String getCostOfInvestment ()
    {
        return CostOfInvestment;
    }

    public void setCostOfInvestment (String CostOfInvestment)
    {
        this.CostOfInvestment = CostOfInvestment;
    }

    public String getCurrentFundValue ()
{
    return CurrentFundValue;
}

    public void setCurrentFundValue (String CurrentFundValue)
    {
        this.CurrentFundValue = CurrentFundValue;
    }

    public String getFolioNumber ()
{
    return FolioNumber;
}

    public void setFolioNumber (String FolioNumber)
    {
        this.FolioNumber = FolioNumber;
    }

    public String getMinSwitchOutAmt ()
    {
        return MinSwitchOutAmt;
    }

    public void setMinSwitchOutAmt (String MinSwitchOutAmt)
    {
        this.MinSwitchOutAmt = MinSwitchOutAmt;
    }

    public String getLotSize ()
    {
        return LotSize;
    }

    public void setLotSize (String LotSize)
    {
        this.LotSize = LotSize;
    }

    public String getLatestNAV ()
    {
        return LatestNAV;
    }

    public void setLatestNAV (String LatestNAV)
    {
        this.LatestNAV = LatestNAV;
    }

    public String getSIPAggrAmount ()
    {
        return SIPAggrAmount;
    }

    public void setSIPAggrAmount (String SIPAggrAmount)
    {
        this.SIPAggrAmount = SIPAggrAmount;
    }

    public String getMinSipInstallmentAmount ()
    {
        return MinSipInstallmentAmount;
    }

    public void setMinSipInstallmentAmount (String MinSipInstallmentAmount)
    {
        this.MinSipInstallmentAmount = MinSipInstallmentAmount;
    }

    public String getRecommendationStatus ()
{
    return RecommendationStatus;
}

    public void setRecommendationStatus (String RecommendationStatus)
    {
        this.RecommendationStatus = RecommendationStatus;
    }

    public String getFundRiskRating ()
{
    return FundRiskRating;
}

    public void setFundRiskRating (String FundRiskRating)
    {
        this.FundRiskRating = FundRiskRating;
    }

    public String getSettlementBankCode ()
    {
        return SettlementBankCode;
    }

    public void setSettlementBankCode (String SettlementBankCode)
    {
        this.SettlementBankCode = SettlementBankCode;
    }

    public String getMinSipInstallmentMonth ()
    {
        return MinSipInstallmentMonth;
    }

    public void setMinSipInstallmentMonth (String MinSipInstallmentMonth)
    {
        this.MinSipInstallmentMonth = MinSipInstallmentMonth;
    }

    public String getSchemeName ()
    {
        return SchemeName;
    }

    public void setSchemeName (String SchemeName)
    {
        this.SchemeName = SchemeName;
    }

    public String getCurrentUnit ()
{
    return CurrentUnit;
}

    public void setCurrentUnit (String CurrentUnit)
    {
        this.CurrentUnit = CurrentUnit;
    }

    public String getAwaitingHoldingUnit ()
    {
        return AwaitingHoldingUnit;
    }

    public void setAwaitingHoldingUnit (String AwaitingHoldingUnit)
    {
        this.AwaitingHoldingUnit = AwaitingHoldingUnit;
    }

    public String getOptions ()
{
    return Options;
}

    public void setOptions (String Options)
    {
        this.Options = Options;
    }

    public String getAssetClassCode ()
    {
        return AssetClassCode;
    }

    public void setAssetClassCode (String AssetClassCode)
    {
        this.AssetClassCode = AssetClassCode;
    }

    public String getExistingAmount ()
    {
        return ExistingAmount;
    }

    public void setExistingAmount (String ExistingAmount)
    {
        this.ExistingAmount = ExistingAmount;
    }

    public String getAdditionalPurchaseAmount ()
    {
        return AdditionalPurchaseAmount;
    }

    public void setAdditionalPurchaseAmount (String AdditionalPurchaseAmount)
    {
        this.AdditionalPurchaseAmount = AdditionalPurchaseAmount;
    }

    public String getMinRedeemUnit ()
    {
        return MinRedeemUnit;
    }

    public void setMinRedeemUnit (String MinRedeemUnit)
    {
        this.MinRedeemUnit = MinRedeemUnit;
    }

    public String getExistingUnits ()
    {
        return ExistingUnits;
    }

    public void setExistingUnits (String ExistingUnits)
    {
        this.ExistingUnits = ExistingUnits;
    }

    public String getValueResearchRating ()
{
    return ValueResearchRating;
}

    public void setValueResearchRating (String ValueResearchRating)
    {
        this.ValueResearchRating = ValueResearchRating;
    }

    public String getIsSWPAllowed ()
    {
        return IsSWPAllowed;
    }

    public void setIsSWPAllowed (String IsSWPAllowed)
    {
        this.IsSWPAllowed = IsSWPAllowed;
    }

    public String getIsHasHolding ()
    {
        return IsHasHolding;
    }

    public void setIsHasHolding (String IsHasHolding)
    {
        this.IsHasHolding = IsHasHolding;
    }

    public String getNature ()
{
    return Nature;
}

    public void setNature (String Nature)
    {
        this.Nature = Nature;
    }

    public String getHasHolding ()
{
    return HasHolding;
}

    public void setHasHolding (String HasHolding)
    {
        this.HasHolding = HasHolding;
    }

    public String getAssetClassName ()
{
    return AssetClassName;
}

    public void setAssetClassName (String AssetClassName)
    {
        this.AssetClassName = AssetClassName;
    }

    public String getIsDividend ()
    {
        return IsDividend;
    }

    public void setIsDividend (String IsDividend)
    {
        this.IsDividend = IsDividend;
    }

    public String getRedeemAllowed ()
    {
        return RedeemAllowed;
    }

    public void setRedeemAllowed (String RedeemAllowed)
    {
        this.RedeemAllowed = RedeemAllowed;
    }

    public String getAMCName ()
{
    return AMCName;
}

    public void setAMCName (String AMCName)
    {
        this.AMCName = AMCName;
    }

    public String getSipStartDates ()
{
    return SipStartDates;
}

    public void setSipStartDates (String SipStartDates)
    {
        this.SipStartDates = SipStartDates;
    }

    public String getMinRedeemAmount ()
    {
        return MinRedeemAmount;
    }

    public void setMinRedeemAmount (String MinRedeemAmount)
    {
        this.MinRedeemAmount = MinRedeemAmount;
    }

    public String getSwitchOutAllowed ()
    {
        return SwitchOutAllowed;
    }

    public void setSwitchOutAllowed (String SwitchOutAllowed)
    {
        this.SwitchOutAllowed = SwitchOutAllowed;
    }

    public String getIsSipAllowed ()
    {
        return IsSipAllowed;
    }

    public void setIsSipAllowed (String IsSipAllowed)
    {
        this.IsSipAllowed = IsSipAllowed;
    }

    public String getClientCode ()
    {
        return ClientCode;
    }

    public void setClientCode (String ClientCode)
    {
        this.ClientCode = ClientCode;
    }

    public String getAwaitingHoldingFundValue ()
    {
        return AwaitingHoldingFundValue;
    }

    public void setAwaitingHoldingFundValue (String AwaitingHoldingFundValue)
    {
        this.AwaitingHoldingFundValue = AwaitingHoldingFundValue;
    }

    public String getEffectiveDate ()
    {
        return EffectiveDate;
    }

    public void setEffectiveDate (String EffectiveDate)
    {
        this.EffectiveDate = EffectiveDate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TranType = "+TranType+", DividendOption = "+DividendOption+", ExpiryDate = "+ExpiryDate+", OfferLink = "+OfferLink+", MinimumInvestmentAmount = "+MinimumInvestmentAmount+", IsSwitchAllowed = "+IsSwitchAllowed+", MinSWPUnit = "+MinSWPUnit+", AMCCode = "+AMCCode+", IsSTPAllowed = "+IsSTPAllowed+", MinSwitchOutAmmount = "+MinSwitchOutAmmount+", IsRedeemAllowed = "+IsRedeemAllowed+", IsPurchaseAllowed = "+IsPurchaseAllowed+", SchemeCode = "+SchemeCode+", CostOfInvestment = "+CostOfInvestment+", CurrentFundValue = "+CurrentFundValue+", FolioNumber = "+FolioNumber+", MinSwitchOutAmt = "+MinSwitchOutAmt+", LotSize = "+LotSize+", LatestNAV = "+LatestNAV+", SIPAggrAmount = "+SIPAggrAmount+", MinSipInstallmentAmount = "+MinSipInstallmentAmount+", RecommendationStatus = "+RecommendationStatus+", FundRiskRating = "+FundRiskRating+", SettlementBankCode = "+SettlementBankCode+", MinSipInstallmentMonth = "+MinSipInstallmentMonth+", SchemeName = "+SchemeName+", CurrentUnit = "+CurrentUnit+", AwaitingHoldingUnit = "+AwaitingHoldingUnit+", Options = "+Options+", AssetClassCode = "+AssetClassCode+", ExistingAmount = "+ExistingAmount+", AdditionalPurchaseAmount = "+AdditionalPurchaseAmount+", MinRedeemUnit = "+MinRedeemUnit+", ExistingUnits = "+ExistingUnits+", ValueResearchRating = "+ValueResearchRating+", IsSWPAllowed = "+IsSWPAllowed+", IsHasHolding = "+IsHasHolding+", Nature = "+Nature+", HasHolding = "+HasHolding+", AssetClassName = "+AssetClassName+", IsDividend = "+IsDividend+", RedeemAllowed = "+RedeemAllowed+", AMCName = "+AMCName+", SipStartDates = "+SipStartDates+", MinRedeemAmount = "+MinRedeemAmount+", SwitchOutAllowed = "+SwitchOutAllowed+", IsSipAllowed = "+IsSipAllowed+", ClientCode = "+ClientCode+", AwaitingHoldingFundValue = "+AwaitingHoldingFundValue+", EffectiveDate = "+EffectiveDate+"]";
    }
}
