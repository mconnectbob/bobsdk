package com.bob.bobapp.api.response_object;

import com.google.gson.annotations.SerializedName;

public class SIPDueReportResponse{
	private String Client_Name;
	private boolean isSelected=false;

	private String RequestId;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	private String Amount;

	private String  FolioNo;

	private String EndDate;

	private String NextInstallmentDate;

	private String Type;

	private String Fund_Name;

	private String SchemeCode;

	private String Frequency;

	private String To_FundName;

	private String DueDate;

	private String Order_No;

	public String getClient_Name ()
	{
		return Client_Name;
	}

	public void setClient_Name (String Client_Name)
	{
		this.Client_Name = Client_Name;
	}

	public String getRequestId ()
	{
		return RequestId;
	}

	public void setRequestId (String RequestId)
	{
		this.RequestId = RequestId;
	}

	public String getAmount ()
	{
		return Amount;
	}

	public void setAmount (String Amount)
	{
		this.Amount = Amount;
	}

	public String getFolioNo ()
	{
		return FolioNo;
	}

	public void setFolioNo (String FolioNo)
	{
		this.FolioNo = FolioNo;
	}

	public String getEndDate ()
	{
		return EndDate;
	}

	public void setEndDate (String EndDate)
	{
		this.EndDate = EndDate;
	}

	public String getNextInstallmentDate ()
	{
		return NextInstallmentDate;
	}

	public void setNextInstallmentDate (String NextInstallmentDate)
	{
		this.NextInstallmentDate = NextInstallmentDate;
	}

	public String getType ()
	{
		return Type;
	}

	public void setType (String Type)
	{
		this.Type = Type;
	}

	public String getFund_Name ()
	{
		return Fund_Name;
	}

	public void setFund_Name (String Fund_Name)
	{
		this.Fund_Name = Fund_Name;
	}

	public String getSchemeCode ()
	{
		return SchemeCode;
	}

	public void setSchemeCode (String SchemeCode)
	{
		this.SchemeCode = SchemeCode;
	}

	public String getFrequency ()
	{
		return Frequency;
	}

	public void setFrequency (String Frequency)
	{
		this.Frequency = Frequency;
	}

	public String getTo_FundName ()
	{
		return To_FundName;
	}

	public void setTo_FundName (String To_FundName)
	{
		this.To_FundName = To_FundName;
	}

	public String getDueDate ()
	{
		return DueDate;
	}

	public void setDueDate (String DueDate)
	{
		this.DueDate = DueDate;
	}

	public String getOrder_No ()
	{
		return Order_No;
	}

	public void setOrder_No (String Order_No)
	{
		this.Order_No = Order_No;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [Client_Name = "+Client_Name+", RequestId = "+RequestId+", Amount = "+Amount+", FolioNo = "+FolioNo+", EndDate = "+EndDate+", NextInstallmentDate = "+NextInstallmentDate+", Type = "+Type+", Fund_Name = "+Fund_Name+", SchemeCode = "+SchemeCode+", Frequency = "+Frequency+", To_FundName = "+To_FundName+", DueDate = "+DueDate+", Order_No = "+Order_No+"]";
	}
}