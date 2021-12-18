package com.bob.bobapp.api.response_object;

public class BuyConfirmResponse
{
    private String Status;

    private String ICDID;

    private String  IsEmail;

    private String Message;

    private String RequestID;

    private String ErrorType;

    private String Paymentlink;

    private String SuccessFlag;

    private String ValidationCode;

    private String OrderNo;

    private String MessageType;

    private String tranType;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getICDID ()
    {
        return ICDID;
    }

    public void setICDID (String ICDID)
    {
        this.ICDID = ICDID;
    }

    public String getIsEmail ()
{
    return IsEmail;
}

    public void setIsEmail (String IsEmail)
    {
        this.IsEmail = IsEmail;
    }

    public String getMessage ()
{
    return Message;
}

    public void setMessage (String Message)
    {
        this.Message = Message;
    }

    public String getRequestID ()
    {
        return RequestID;
    }

    public void setRequestID (String RequestID)
    {
        this.RequestID = RequestID;
    }

    public String getErrorType ()
{
    return ErrorType;
}

    public void setErrorType (String ErrorType)
    {
        this.ErrorType = ErrorType;
    }

    public String getPaymentlink ()
{
    return Paymentlink;
}

    public void setPaymentlink (String Paymentlink)
    {
        this.Paymentlink = Paymentlink;
    }

    public String getSuccessFlag ()
    {
        return SuccessFlag;
    }

    public void setSuccessFlag (String SuccessFlag)
    {
        this.SuccessFlag = SuccessFlag;
    }

    public String getValidationCode ()
{
    return ValidationCode;
}

    public void setValidationCode (String ValidationCode)
    {
        this.ValidationCode = ValidationCode;
    }

    public String getOrderNo ()
    {
        return OrderNo;
    }

    public void setOrderNo (String OrderNo)
    {
        this.OrderNo = OrderNo;
    }

    public String getMessageType ()
    {
        return MessageType;
    }

    public void setMessageType (String MessageType)
    {
        this.MessageType = MessageType;
    }

    public String getTranType ()
    {
        return tranType;
    }

    public void setTranType (String tranType)
    {
        this.tranType = tranType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", ICDID = "+ICDID+", IsEmail = "+IsEmail+", Message = "+Message+", RequestID = "+RequestID+", ErrorType = "+ErrorType+", Paymentlink = "+Paymentlink+", SuccessFlag = "+SuccessFlag+", ValidationCode = "+ValidationCode+", OrderNo = "+OrderNo+", MessageType = "+MessageType+", tranType = "+tranType+"]";
    }
}
