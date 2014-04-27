package org.p12ct.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.p12ct.beans.EntryDetails;
import org.p12ct.comparator.EntryDetailComparator;
import org.p12ct.enums.EntryTypeEnum;

public class KeyStoreUtils
{

    private static KeyStore getInstance() throws GeneralSecurityException
    {
	return KeyStore.getInstance("PKCS12", "BC");
    }
   
    public static KeyStore getEmptyKeyStore() throws IOException, GeneralSecurityException 
    {
	KeyStore ks = KeyStoreUtils.getInstance();
	ks.load(null,null);
	return ks;
    }
    
    public static KeyStore loadKeyStore(String keyStoreFileName, String keyStorePin) throws FileNotFoundException, IOException, GeneralSecurityException
    {
	FileInputStream fin = null;
	try
	{
	    KeyStore ks = KeyStoreUtils.getInstance();
	    fin = new FileInputStream(keyStoreFileName);
	    ks.load(fin, keyStorePin.toCharArray());
	    return ks;
	}
	finally
	{
	    if (fin != null)
	    {
		try { fin.close(); } catch(Exception exc) {}
	    }
	}
    }
    
    public static List<String> getAliasList(KeyStore ks) throws GeneralSecurityException
    {
	Enumeration<String> aliasEnum = ks.aliases();
	List<String> orderedAliasList = new ArrayList<String>();
	while (aliasEnum.hasMoreElements()) 
	{
	    orderedAliasList.add(aliasEnum.nextElement());
	}

	Collections.sort(orderedAliasList);
	return orderedAliasList;
    }
    
    public static List<EntryDetails> getAliasList(KeyStore ks, String pin) throws GeneralSecurityException
    {
	Enumeration<String> aliasEnum = ks.aliases();
	List<EntryDetails> orderedAliasList = new ArrayList<EntryDetails>();
	KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(pin.toCharArray());
	
	while (aliasEnum.hasMoreElements()) 
	{
	    String alias = aliasEnum.nextElement();
	    EntryTypeEnum entryType;
	    if (ks.isCertificateEntry(alias) == true)
	    {
		entryType = EntryTypeEnum.CERTIFICATE;
	    }
	    else
	    {
		if (ks.getEntry(alias, protectionParam) instanceof PrivateKeyEntry)
		{
		    entryType = EntryTypeEnum.PRIVATE_KEY;
		}
		else
		{
		    entryType = EntryTypeEnum.SECRET_KEY;
		}
	    }
	    
	    orderedAliasList.add(new EntryDetails(alias , entryType));
	}

	Collections.sort(orderedAliasList, new EntryDetailComparator());
	return orderedAliasList;
    }

}
