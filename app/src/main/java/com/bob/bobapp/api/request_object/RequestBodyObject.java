package com.bob.bobapp.api.request_object;
import com.bob.bobapp.api.bean.RiskProfileQuestionnaireBean;
import com.google.gson.annotations.SerializedName;

public class RequestBodyObject
{
    private static RequestBodyObject requestBodyObject = null;

    @SerializedName("UserId")
    private String userId;

    @SerializedName("FundHouses")
    private String FundHouses;

    public String getFundOptionsCommaSeparated() {
        return FundOptionsCommaSeparated;
    }

    public void setFundOptionsCommaSeparated(String fundOptionsCommaSeparated) {
        FundOptionsCommaSeparated = fundOptionsCommaSeparated;
    }

    @SerializedName("FundOptionsCommaSeparated")
    private String FundOptionsCommaSeparated;

    public String getFundTypes() {
        return FundTypes;
    }

    public void setFundTypes(String fundTypes) {
        FundTypes = fundTypes;
    }

    @SerializedName("FundTypes")
    private String FundTypes;

    public String getFundHouses() {
        return FundHouses;
    }

    public void setFundHouses(String fundHouses) {
        FundHouses = fundHouses;
    }

    @SerializedName("SearchString")
    private String SearchString;

    @SerializedName("AssetTypes")
    private String AssetTypes;

    public String getAssetTypes() {
        return AssetTypes;
    }

    public void setAssetTypes(String assetTypes) {
        AssetTypes = assetTypes;
    }

    public String getSearchString() {
        return SearchString;
    }

    public void setSearchString(String searchString) {
        SearchString = searchString;
    }

    @SerializedName("UserType")
    private String userType;

    @SerializedName("UserCode")
    private String userCode;

    @SerializedName("LastBusinessDate")
    private String lastBusinessDate;

    @SerializedName("CurrencyCode")
    private String currencyCode;

    @SerializedName("AmountDenomination")
    private String amountDenomination;
 @SerializedName("Amount")
    private String Amount;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    @SerializedName("AccountLevel")
    private String accountLevel;

    @SerializedName("ClientCode")
    private String clientCode;

    @SerializedName("ClientType")
    private String ClientType;

    @SerializedName("IsFundware")
    private String IsFundware;

    @SerializedName("ParentChannelID")
    private String parentChannelID;

    @SerializedName("AllocationType")
    private String AllocationType;

    @SerializedName("IndexType")
    private String IndexType;

    @SerializedName("FromDate")
    private String FromDate;

    @SerializedName("ReminderPeriod")
    private String ReminderPeriod;

    @SerializedName("MWIClientCode")
    private String MWIClientCode;

    @SerializedName("SchemeCode")
    private String SchemeCode;

    @SerializedName("FundOption")
    private String FundOption;

    @SerializedName("TransactionType")
    private String TransactionType;



    @SerializedName("TillDate")
    private String TillDate;

    @SerializedName("RiskProfileQuestionnaire")
    private RiskProfileQuestionnaireBean RiskProfileQuestionnaire;

    @SerializedName("LatestNAV")
    private String LatestNAV;

    @SerializedName("SchemeName")
    private String SchemeName;

    @SerializedName("ValueResearchRating")
    private String ValueResearchRating;

    @SerializedName("FundRiskRating")
    private String FundRiskRating;

    @SerializedName("AssetClassCode")
    private String AssetClassCode;

    @SerializedName("inputmode")
    private String inputmode;

    @SerializedName("TxnAmountUnit")
    private String TxnAmountUnit;

    @SerializedName("IsDividend")
    private String IsDividend;

    @SerializedName("DividendOption")
    private String DividendOption;

    @SerializedName("L4ClientCode")
    private String L4ClientCode;

    @SerializedName("CurrentUnits")
    private String CurrentUnits;

    @SerializedName("CurrentFundValue")
    private String CurrentFundValue;

    @SerializedName("CostofInvestment")
    private String CostofInvestment;

    @SerializedName("frequency")
    private String frequency;

    public String getFrequencyss() {
        return Frequencyss;
    }

    public void setFrequencyss(String frequencyss) {
        Frequencyss = frequencyss;
    }

    @SerializedName("Frequency")
    private String Frequencyss;

    @SerializedName("AllorPartial")
    private String AllorPartial;

    @SerializedName("AmountOrUnit")
    private String AmountOrUnit;

    @SerializedName("StartDate")
    private String StartDate;

    @SerializedName("TargetFundCode")
    private String TargetFundCode;

    @SerializedName("Period")
    private String Period;

    @SerializedName("NoofMonth")
    private String NoofMonth;

    @SerializedName("ICDID")
    private String ICDID;

    @SerializedName("SettlementBankCode")
    private String SettlementBankCode;

    @SerializedName("SipStartDates")
    private String SipStartDates;

    @SerializedName("NomineeName1")
    private String NomineeName1;

    @SerializedName("DateOfBirth1")
    private String DateOfBirth1;

    @SerializedName("NomineeRelationship1")
    private String NomineeRelationship1;

    @SerializedName("NomineeShare1")
    private String NomineeShare1;

    @SerializedName("NomineeAddress1")
    private String NomineeAddress1;

    @SerializedName("NomineeIsMinor1")
    private String NomineeIsMinor1;

    @SerializedName("NomineeName2")
    private String NomineeName2;

    @SerializedName("DateOfBirth2")
    private String DateOfBirth2;

    @SerializedName("NomineeRelationship2")
    private String NomineeRelationship2;

    @SerializedName("NomineeShare2")
    private String NomineeShare2;

    @SerializedName("NomineeAddress2")
    private String NomineeAddress2;

    @SerializedName("NomineeIsMinor2")
    private String NomineeIsMinor2;

    @SerializedName("GuardianName2")
    private String GuardianName2;

    @SerializedName("NomineeName3")
    private String NomineeName3;

    @SerializedName("DateOfBirth3")
    private String DateOfBirth3;

    @SerializedName("NomineeRelationship3")
    private String NomineeRelationship3;

    @SerializedName("NomineeShare3")
    private String NomineeShare3;

    @SerializedName("NomineeAddress3")
    private String NomineeAddress3;

    @SerializedName("NomineeIsMinor3")
    private String NomineeIsMinor3;

    @SerializedName("GuardianName3")
    private String GuardianName3;

    @SerializedName("IsApplyNominee")
    private String IsApplyNominee;

    @SerializedName("FolioNo")
    private String FolioNo;

    @SerializedName("ChannelID")
    private String ChannelID;

    @SerializedName("IsPerpetual")
    private String IsPerpetual;

    @SerializedName("NextInstallmentDate")
    private String NextInstallmentDate;

    @SerializedName("TargetFundName")
    private String TargetFundName;

    @SerializedName("RequestId")
    private int RequestId;

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    @SerializedName("RequestType")
    private String  RequestType;

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    @SerializedName("OrderNumber")
    private int OrderNumber;

    @SerializedName("FundCode")
    private String FundCode;

    @SerializedName("amtOrUnit")
    private String amtOrUnit;

    public String getAmtOrUnit() {
        return amtOrUnit;
    }

    public void setAmtOrUnit(String amtOrUnit) {
        this.amtOrUnit = amtOrUnit;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }

    @SerializedName("Folio")
    private String Folio;

    public String getSipStartDate() {
        return SipStartDate;
    }

    public void setSipStartDate(String sipStartDate) {
        SipStartDate = sipStartDate;
    }

    @SerializedName("SipStartDate")
    private String SipStartDate;

    public String getFundName() {
        return FundName;
    }

    public void setFundName(String fundName) {
        FundName = fundName;
    }

    @SerializedName("FundName")
    private String FundName;

    public String getTranType() {
        return TranType;
    }

    public void setTranType(String tranType) {
        TranType = tranType;
    }

    @SerializedName("TranType")
    private String TranType;

    @SerializedName("PaymentMode")
    private String PaymentMode;

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    @SerializedName("TranUnit")
    private String TranUnit;

    public String getTranUnit() {
        return TranUnit;
    }

    public void setTranUnit(String tranUnit) {
        TranUnit = tranUnit;
    }

    public String getTranAmt() {
        return TranAmt;
    }

    public void setTranAmt(String tranAmt) {
        TranAmt = tranAmt;
    }

    @SerializedName("TranAmt")
    private String TranAmt;

    @SerializedName("TransactionAmountUnit")
    private String TransactionAmountUnit;

    public String getTransactionAmountUnit() {
        return TransactionAmountUnit;
    }

    public void setTransactionAmountUnit(String transactionAmountUnit) {
        TransactionAmountUnit = transactionAmountUnit;
    }

    public int getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getFundCode() {
        return FundCode;
    }

    public void setFundCode(String fundCode) {
        FundCode = fundCode;
    }


    @SerializedName("Code")
    private String  Code;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getAssetClassName() {
        return AssetClassName;
    }

    public void setAssetClassName(String assetClassName) {
        AssetClassName = assetClassName;
    }

    @SerializedName("AssetClassName")
    private String AssetClassName;

    public String getDebitBankSource() {
        return DebitBankSource;
    }

    public void setDebitBankSource(String debitBankSource) {
        DebitBankSource = debitBankSource;
    }

    public String getBankAccountNo() {
        return BankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        BankAccountNo = bankAccountNo;
    }

    @SerializedName("DebitBankSource")
    private String DebitBankSource;

    public String getFundOptions() {
        return FundOptions;
    }

    public void setFundOptions(String fundOptions) {
        FundOptions = fundOptions;
    }

    @SerializedName("FundOptions")
    private String FundOptions;

    @SerializedName("BankAccountNo")
    private String BankAccountNo;

    @SerializedName("BankAccountNumber")
    private String BankAccountNumber;

    public String getBankAccountNumber() {
        return BankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        BankAccountNumber = bankAccountNumber;
    }

    @SerializedName("ClientMobileNo")
    private String ClientMobileNo;

    @SerializedName("ClientUCC")
    private String ClientUCC;

    public String getMpinEncValue() {
        return MpinEncValue;
    }

    public void setMpinEncValue(String mpinEncValue) {
        MpinEncValue = mpinEncValue;
    }

    @SerializedName("MpinEncValue")
    private String MpinEncValue;

    public String getRegistrationRefId() {
        return RegistrationRefId;
    }

    public void setRegistrationRefId(String registrationRefId) {
        RegistrationRefId = registrationRefId;
    }

    @SerializedName("RegistrationRefId")
    private String RegistrationRefId;

    public String getMpinMode() {
        return MpinMode;
    }

    public void setMpinMode(String mpinMode) {
        MpinMode = mpinMode;
    }

    @SerializedName("MpinMode")
    private String MpinMode;

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    @SerializedName("AuthToken")
    private String AuthToken;

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    @SerializedName("SessionID")
    private String SessionID;

    public String getSessionNo() {
        return SessionNo;
    }

    public void setSessionNo(String sessionNo) {
        SessionNo = sessionNo;
    }

    @SerializedName("SessionNo")
    private String SessionNo;

    public String getClientUCC() {
        return ClientUCC;
    }

    public void setClientUCC(String clientUCC) {
        ClientUCC = clientUCC;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }

    @SerializedName("Channel")
    private String Channel;

    public String getExternalCode() {
        return ExternalCode;
    }

    public void setExternalCode(String externalCode) {
        ExternalCode = externalCode;
    }

    @SerializedName("ExternalCode")
    private String ExternalCode;

    public String getOTPPassword() {
        return OTPPassword;
    }

    public void setOTPPassword(String OTPPassword) {
        this.OTPPassword = OTPPassword;
    }

    @SerializedName("OTPPassword")
    private String OTPPassword;

    public String getClientMobileNo() {
        return ClientMobileNo;
    }

    public void setClientMobileNo(String clientMobileNo) {
        ClientMobileNo = clientMobileNo;
    }

    public String getClientIP() {
        return ClientIP;
    }

    public void setClientIP(String clientIP) {
        ClientIP = clientIP;
    }

    @SerializedName("ClientIP")
    private String ClientIP;

    public String getOrderChannelID() {
        return OrderChannelID;
    }

    public void setOrderChannelID(String orderChannelID) {
        OrderChannelID = orderChannelID;
    }

    @SerializedName("OrderChannelID")
    private String OrderChannelID;

    public String getPlatform_id() {
        return Platform_id;
    }

    public void setPlatform_id(String platform_id) {
        Platform_id = platform_id;
    }

    @SerializedName("Platform_id")
    private String Platform_id;

    @SerializedName("Installments")
    private String Installments;

    public String getInstallments() {
        return Installments;
    }

    public void setInstallments(String installments) {
        Installments = installments;
    }

    public String getSwitchDividendOption() {
        return SwitchDividendOption;
    }

    public void setSwitchDividendOption(String switchDividendOption) {
        SwitchDividendOption = switchDividendOption;
    }

    @SerializedName("SwitchDividendOption")
    private String SwitchDividendOption;

    public String getDebitBankCode() {
        return DebitBankCode;
    }

    public void setDebitBankCode(String debitBankCode) {
        DebitBankCode = debitBankCode;
    }

    @SerializedName("DebitBankCode")
    private String DebitBankCode;

    @SerializedName("valueDate")
    private String valueDate;

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    public String getTargetFundName() {
        return TargetFundName;
    }

    public void setTargetFundName(String targetFundName) {
        TargetFundName = targetFundName;
    }

    public String getNextInstallmentDate() {
        return NextInstallmentDate;
    }

    public void setNextInstallmentDate(String nextInstallmentDate) {
        NextInstallmentDate = nextInstallmentDate;
    }

    public String getIsPerpetual() {
        return IsPerpetual;
    }

    public void setIsPerpetual(String isPerpetual) {
        IsPerpetual = isPerpetual;
    }



    public RiskProfileQuestionnaireBean getRiskProfileQuestionnaire() {
        return RiskProfileQuestionnaire;
    }

    public void setRiskProfileQuestionnaire(RiskProfileQuestionnaireBean riskProfileQuestionnaire) {
        RiskProfileQuestionnaire = riskProfileQuestionnaire;
    }

    public String getMWIClientCode() {
        return MWIClientCode;
    }

    public void setMWIClientCode(String MWIClientCode) {
        this.MWIClientCode = MWIClientCode;
    }

    public String getSchemeCode() {
        return SchemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        SchemeCode = schemeCode;
    }

    public String getReminderPeriod() {
        return ReminderPeriod;
    }

    public void setReminderPeriod(String reminderPeriod) {
        ReminderPeriod = reminderPeriod;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getIndexType() {
        return IndexType;
    }

    public void setIndexType(String indexType) {
        IndexType = indexType;
    }

    public String getAllocationType() {
        return AllocationType;
    }

    public void setAllocationType(String allocationType) {
        AllocationType = allocationType;
    }

    public String getIsFundware() {
        return IsFundware;
    }

    public void setIsFundware(String isFundware) {
        IsFundware = isFundware;
    }

    public String getClientType() {
        return ClientType;
    }

    public void setClientType(String clientType) {
        ClientType = clientType;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getParentChannelID() {
        return parentChannelID;
    }

    public void setParentChannelID(String parentChannelID) {
        this.parentChannelID = parentChannelID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getLastBusinessDate() {
        return lastBusinessDate;
    }

    public void setLastBusinessDate(String lastBusinessDate) {
        this.lastBusinessDate = lastBusinessDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAmountDenomination() {
        return amountDenomination;
    }

    public void setAmountDenomination(String amountDenomination) {
        this.amountDenomination = amountDenomination;
    }

    public String getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(String accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getTillDate() {
        return TillDate;
    }

    public void setTillDate(String tillDate) {
        TillDate = tillDate;
    }

    public String getLatestNAV() {
        return LatestNAV;
    }

    public void setLatestNAV(String latestNAV) {
        LatestNAV = latestNAV;
    }

    public String getSchemeName() {
        return SchemeName;
    }

    public void setSchemeName(String schemeName) {
        SchemeName = schemeName;
    }

    public String getValueResearchRating() {
        return ValueResearchRating;
    }

    public void setValueResearchRating(String valueResearchRating) {
        ValueResearchRating = valueResearchRating;
    }

    public String getFundRiskRating() {
        return FundRiskRating;
    }

    public void setFundRiskRating(String fundRiskRating) {
        FundRiskRating = fundRiskRating;
    }

    public String getAssetClassCode() {
        return AssetClassCode;
    }

    public void setAssetClassCode(String assetClassCode) {
        AssetClassCode = assetClassCode;
    }

    public String getInputmode() {
        return inputmode;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public String getTxnAmountUnit() {
        return TxnAmountUnit;
    }

    public void setTxnAmountUnit(String txnAmountUnit) {
        TxnAmountUnit = txnAmountUnit;
    }

    public String getIsDividend() {
        return IsDividend;
    }

    public void setIsDividend(String isDividend) {
        IsDividend = isDividend;
    }

    public String getDividendOption() {
        return DividendOption;
    }

    public void setDividendOption(String dividendOption) {
        DividendOption = dividendOption;
    }

    public String getL4ClientCode() {
        return L4ClientCode;
    }

    public void setL4ClientCode(String l4ClientCode) {
        L4ClientCode = l4ClientCode;
    }

    public String getCurrentUnits() {
        return CurrentUnits;
    }

    public void setCurrentUnits(String currentUnits) {
        CurrentUnits = currentUnits;
    }

    public String getCurrentFundValue() {
        return CurrentFundValue;
    }

    public void setCurrentFundValue(String currentFundValue) {
        CurrentFundValue = currentFundValue;
    }

    public String getCostofInvestment() {
        return CostofInvestment;
    }

    public void setCostofInvestment(String costofInvestment) {
        CostofInvestment = costofInvestment;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getAllorPartial() {
        return AllorPartial;
    }

    public void setAllorPartial(String allorPartial) {
        AllorPartial = allorPartial;
    }

    public String getAmountOrUnit() {
        return AmountOrUnit;
    }

    public void setAmountOrUnit(String amountOrUnit) {
        AmountOrUnit = amountOrUnit;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getTargetFundCode() {
        return TargetFundCode;
    }

    public void setTargetFundCode(String targetFundCode) {
        TargetFundCode = targetFundCode;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getNoofMonth() {
        return NoofMonth;
    }

    public void setNoofMonth(String noofMonth) {
        NoofMonth = noofMonth;
    }

    public String getICDID() {
        return ICDID;
    }

    public void setICDID(String ICDID) {
        this.ICDID = ICDID;
    }

    public String getSettlementBankCode() {
        return SettlementBankCode;
    }

    public void setSettlementBankCode(String settlementBankCode) {
        SettlementBankCode = settlementBankCode;
    }

    public String getSipStartDates() {
        return SipStartDates;
    }

    public void setSipStartDates(String sipStartDates) {
        SipStartDates = sipStartDates;
    }

    public String getNomineeName1() {
        return NomineeName1;
    }

    public void setNomineeName1(String nomineeName1) {
        NomineeName1 = nomineeName1;
    }

    public String getDateOfBirth1() {
        return DateOfBirth1;
    }

    public void setDateOfBirth1(String dateOfBirth1) {
        DateOfBirth1 = dateOfBirth1;
    }

    public String getNomineeRelationship1() {
        return NomineeRelationship1;
    }

    public void setNomineeRelationship1(String nomineeRelationship1) {
        NomineeRelationship1 = nomineeRelationship1;
    }

    public String getNomineeShare1() {
        return NomineeShare1;
    }

    public void setNomineeShare1(String nomineeShare1) {
        NomineeShare1 = nomineeShare1;
    }

    public String getNomineeAddress1() {
        return NomineeAddress1;
    }

    public void setNomineeAddress1(String nomineeAddress1) {
        NomineeAddress1 = nomineeAddress1;
    }

    public String getNomineeIsMinor1() {
        return NomineeIsMinor1;
    }

    public void setNomineeIsMinor1(String nomineeIsMinor1) {
        NomineeIsMinor1 = nomineeIsMinor1;
    }

    public String getNomineeName2() {
        return NomineeName2;
    }

    public void setNomineeName2(String nomineeName2) {
        NomineeName2 = nomineeName2;
    }

    public String getDateOfBirth2() {
        return DateOfBirth2;
    }

    public void setDateOfBirth2(String dateOfBirth2) {
        DateOfBirth2 = dateOfBirth2;
    }

    public String getNomineeRelationship2() {
        return NomineeRelationship2;
    }

    public void setNomineeRelationship2(String nomineeRelationship2) {
        NomineeRelationship2 = nomineeRelationship2;
    }

    public String getNomineeShare2() {
        return NomineeShare2;
    }

    public void setNomineeShare2(String nomineeShare2) {
        NomineeShare2 = nomineeShare2;
    }

    public String getNomineeAddress2() {
        return NomineeAddress2;
    }

    public void setNomineeAddress2(String nomineeAddress2) {
        NomineeAddress2 = nomineeAddress2;
    }

    public String getNomineeIsMinor2() {
        return NomineeIsMinor2;
    }

    public void setNomineeIsMinor2(String nomineeIsMinor2) {
        NomineeIsMinor2 = nomineeIsMinor2;
    }

    public String getGuardianName2() {
        return GuardianName2;
    }

    public void setGuardianName2(String guardianName2) {
        GuardianName2 = guardianName2;
    }

    public String getNomineeName3() {
        return NomineeName3;
    }

    public void setNomineeName3(String nomineeName3) {
        NomineeName3 = nomineeName3;
    }

    public String getDateOfBirth3() {
        return DateOfBirth3;
    }

    public void setDateOfBirth3(String dateOfBirth3) {
        DateOfBirth3 = dateOfBirth3;
    }

    public String getNomineeRelationship3() {
        return NomineeRelationship3;
    }

    public void setNomineeRelationship3(String nomineeRelationship3) {
        NomineeRelationship3 = nomineeRelationship3;
    }

    public String getNomineeShare3() {
        return NomineeShare3;
    }

    public void setNomineeShare3(String nomineeShare3) {
        NomineeShare3 = nomineeShare3;
    }

    public String getNomineeAddress3() {
        return NomineeAddress3;
    }

    public void setNomineeAddress3(String nomineeAddress3) {
        NomineeAddress3 = nomineeAddress3;
    }

    public String getNomineeIsMinor3() {
        return NomineeIsMinor3;
    }

    public void setNomineeIsMinor3(String nomineeIsMinor3) {
        NomineeIsMinor3 = nomineeIsMinor3;
    }

    public String getGuardianName3() {
        return GuardianName3;
    }

    public void setGuardianName3(String guardianName3) {
        GuardianName3 = guardianName3;
    }

    public String getIsApplyNominee() {
        return IsApplyNominee;
    }

    public void setIsApplyNominee(String isApplyNominee) {
        IsApplyNominee = isApplyNominee;
    }

    public String getFundOption() {
        return FundOption;
    }

    public void setFundOption(String fundOption) {
        FundOption = fundOption;
    }

    public String getFolioNo() {
        return FolioNo;
    }

    public void setFolioNo(String folioNo) {
        FolioNo = folioNo;
    }

    public String getChannelID() {
        return ChannelID;
    }

    public void setChannelID(String channelID) {
        ChannelID = channelID;
    }
}
