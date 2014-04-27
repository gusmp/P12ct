package org.p12ct.enums;

public enum EntryTypeEnum
{
    CERTIFICATE("Trusted certificate"), PRIVATE_KEY("Private key"), SECRET_KEY("Secret key");
    
    private String description;
    
    EntryTypeEnum(String value)
    {
	this.description = value;
    }
    
    public String toString()
    {
	return this.description;
    }
}
