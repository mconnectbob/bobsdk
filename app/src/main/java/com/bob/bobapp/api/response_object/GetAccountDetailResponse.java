package com.bob.bobapp.api.response_object;

import java.util.ArrayList;

public class GetAccountDetailResponse
{
    private String  Status;

    private String Description;

    private ArrayList<CustomerBankAccounts>CustomerBankAccounts;


    public ArrayList<CustomerBankAccounts> getCustomerBankAccounts() {
        return CustomerBankAccounts;
    }

    public void setCustomerBankAccounts(ArrayList<CustomerBankAccounts> customerBankAccounts) {
        CustomerBankAccounts = customerBankAccounts;
    }

    public String getStatus ()
{
    return Status;
}

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getDescription ()
{
    return Description;
}

    public void setDescription (String Description)
    {
        this.Description = Description;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Description = "+Description+", CustomerBankAccounts = "+CustomerBankAccounts+"]";
    }
}
