package org.p12ct.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.Security;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.p12ct.beans.EntryDetails;
import org.p12ct.beans.P12InfoBean;
import org.p12ct.exception.PinMismatchException;
import org.p12ct.exception.TargetPkcs12FileNameException;
import org.p12ct.utils.KeyStoreUtils;

public class P12ct
{

    public static void main(String[] args)
    {

	Security.addProvider(new BouncyCastleProvider());

	P12InfoBean p12InfoBean = null;
	try
	{
	    p12InfoBean = getInputData();

	    modifyP12(p12InfoBean);

	    System.out.println("Done!");
	}
	catch (FileNotFoundException exc)
	{
	    System.out.println("PKCS#12 could not be found: " + exc.toString());
	}
	catch (IOException exc)
	{
	    System.out.println("Error reading / writing files from file system: " + exc.toString());
	    exc.printStackTrace();
	}
	catch (GeneralSecurityException exc)
	{
	    System.out.println("Error dealing with PKCS#12 files and cryptographic provider: " + exc.toString());
	}
	catch (PinMismatchException exc)
	{
	    System.out.println("New pin and validation does not match!");
	}
	catch (TargetPkcs12FileNameException exc)
	{
	    System.out.println("Target name cannot be the same as the PKCS#12 origin");
	}
    }

    public static P12InfoBean getInputData() throws IOException, TargetPkcs12FileNameException, PinMismatchException, GeneralSecurityException
    {
	P12InfoBean p12InfoBean = new P12InfoBean();

	System.out.println("Source PKCS#12 file name:");
	p12InfoBean.setPkcs12Name(readInputString(true));

	System.out.println("Target PKCS#12 file name:");
	p12InfoBean.setNewPkcs12Name(readInputString(true));

	p12InfoBean.validatePkcs12Name();

	System.out.println("Pin:");
	p12InfoBean.setPin(readInputString(true));

	System.out.println("New pin:");
	p12InfoBean.setNewPin(readInputString(true));

	System.out.println("New pin (confirmation):");
	p12InfoBean.setNewPinValidation(readInputString(true));

	p12InfoBean.validatePin();
	
	printAliasList(p12InfoBean);
	
	System.out.println("Select the alias number to change (leave it in blank if you don't):");
	p12InfoBean.setAliasNumber(readInputInteger(false));

	if (p12InfoBean.getAliasNumber() != null)
	{
	    System.out.println("New alias:");
	    p12InfoBean.setNewAlias(readInputString(true));
	}

	return p12InfoBean;
    }
    
    public static void printAliasList(P12InfoBean p12InfoBean) throws FileNotFoundException, IOException, GeneralSecurityException
    {
	KeyStore ks = KeyStoreUtils.loadKeyStore(p12InfoBean.getPkcs12Name(), p12InfoBean.getPin());
	
	List<EntryDetails> entryDetailsList = KeyStoreUtils.getAliasList(ks, p12InfoBean.getPin());
	p12InfoBean.setEntryDetailList(entryDetailsList);
	System.out.println("List of alias:");
	for(int i=0; i < entryDetailsList.size(); i++)
	{
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append(i);
	    stringBuilder.append(" - ");
	    stringBuilder.append(entryDetailsList.get(i).getEntryType().toString());
	    stringBuilder.append(" - Alias: ");
	    if (entryDetailsList.get(i).getAlias().length() == 0)
	    {
		stringBuilder.append("<This entry has an empty alias>");
	    }
	    else
	    {
		stringBuilder.append(entryDetailsList.get(i).getAlias());
	    }
	    
	    System.out.println(stringBuilder.toString());
	}

    }

    public static String readInputString(boolean required) throws IOException
    {
	BufferedReader buffIn = new BufferedReader(new InputStreamReader(System.in));
	String input = "";
	int key;

	do
	{
	    while ((key = buffIn.read()) != 13)
	    {
		input += String.valueOf((char) key);
	    }
	    input = input.trim();
	}
	while ((input.length() == 0) && (required == true));

	return input;
    }
    
    public static Integer readInputInteger(boolean required) throws IOException
    {
	String option = readInputString(required);
	if ((option == null) || (option.length() == 0))
	{
	    return null;
	}
	else
	{
	    return Integer.valueOf(option.trim());
	}
    }

    public static void modifyP12(P12InfoBean p12InfoBean) throws FileNotFoundException, IOException, GeneralSecurityException
    {
	FileOutputStream fout = null;

	try
	{
	    KeyStore newKs = KeyStoreUtils.getEmptyKeyStore();
	    fout = new FileOutputStream(p12InfoBean.getNewPkcs12Name());

	    KeyStore ks = KeyStoreUtils.loadKeyStore(p12InfoBean.getPkcs12Name(), p12InfoBean.getPin());
	    KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(p12InfoBean.getPin().toCharArray());
	    
	    for(int i=0; i < p12InfoBean.getEntryDetailList().size(); i++)
	    {
		EntryDetails entryDetails = p12InfoBean.getEntryDetailList().get(i);
		String newAlias = entryDetails.getAlias();
		if ((p12InfoBean.getAliasNumber() != null) && (i == p12InfoBean.getAliasNumber().intValue()))
		{
		    newAlias = p12InfoBean.getNewAlias();
		}
		
		if (ks.isCertificateEntry(entryDetails.getAlias()) == true)
		{
		    newKs.setCertificateEntry(newAlias, ks.getCertificate(entryDetails.getAlias()));
		}
		else if (ks.isKeyEntry(entryDetails.getAlias()) == true)
		{
		    Entry entry = ks.getEntry(entryDetails.getAlias(), protectionParam);
		    newKs.setEntry(newAlias, entry, protectionParam);
		}
	    }
	    
	    /*
	    Enumeration<String> aliasEnum = ks.aliases();
	    while (aliasEnum.hasMoreElements() == true)
	    {
		String alias = (String) aliasEnum.nextElement();
		
		KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(p12InfoBean.getPin().toCharArray());

		if (ks.isCertificateEntry(alias) == true)
		{
		    newKs.setCertificateEntry(alias, ks.getCertificate(alias));
		}
		else if (ks.isKeyEntry(alias) == true)
		{
		    Entry entry = ks.getEntry(alias, protectionParam);
		    newKs.setEntry(alias, entry, protectionParam);
		}
	    }
	    */
	    
	   newKs.store(fout, p12InfoBean.getNewPin().toCharArray());
	}
	finally
	{
	    if (fout != null)
	    {
		try
		{
		    fout.close();
		}
		catch (Exception exc)
		{
		}
	    }
	}
    }
}
