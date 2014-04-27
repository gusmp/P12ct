package org.p12ct.beans;

import java.util.List;

import org.p12ct.exception.PinMismatchException;
import org.p12ct.exception.TargetPkcs12FileNameException;

public class P12InfoBean
{

    private String pkcs12Name;
    private String newPkcs12Name;

    private String pin;
    private String newPin;
    private String newPinValidation;

    private Integer aliasNumber;
    private String newAlias;
    
    private List<EntryDetails> entryDetailList;

    public String getPkcs12Name()
    {
	return pkcs12Name;
    }

    public void setPkcs12Name(String pkcs12Name)
    {
	this.pkcs12Name = pkcs12Name.trim();
    }

    public String getNewPkcs12Name()
    {
	return newPkcs12Name;
    }

    public void setNewPkcs12Name(String newPkcs12Name)
    {
	this.newPkcs12Name = newPkcs12Name.trim();
    }

    public String getPin()
    {
	return pin;
    }

    public void setPin(String pin)
    {
	this.pin = pin.trim();
    }

    public String getNewPin()
    {
	return newPin;
    }

    public void setNewPin(String newPin)
    {
	this.newPin = newPin.trim();
    }

    public String getNewPinValidation()
    {
	return newPinValidation;
    }

    public void setNewPinValidation(String newPinValidation)
    {
	this.newPinValidation = newPinValidation.trim();
    }
    
    public Integer getAliasNumber()
    {
        return aliasNumber;
    }

    public void setAliasNumber(Integer aliasNumber)
    {
        this.aliasNumber = aliasNumber;
    }

    public String getNewAlias()
    {
	return newAlias;
    }

    public void setNewAlias(String newAlias)
    {
	this.newAlias = newAlias.trim();
    }

    public List<EntryDetails> getEntryDetailList()
    {
        return entryDetailList;
    }

    public void setEntryDetailList(List<EntryDetails> entryDetailList)
    {
        this.entryDetailList = entryDetailList;
    }

    public void validatePkcs12Name() throws TargetPkcs12FileNameException
    {
	if (this.pkcs12Name.equalsIgnoreCase(this.newPkcs12Name) == true)
	{
	    throw new TargetPkcs12FileNameException();
	}
    }

    public void validatePin() throws PinMismatchException
    {
	if (this.newPin.equalsIgnoreCase(this.newPinValidation) == false)
	{
	    throw new PinMismatchException();
	}
    }
    
}
