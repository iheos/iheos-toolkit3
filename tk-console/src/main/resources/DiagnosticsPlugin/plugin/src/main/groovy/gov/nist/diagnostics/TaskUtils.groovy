package gov.nist.diagnostics

/*
 * C:\e\Packages\mygradlescripts\DiagnosticsPlugin\plugin\src\main\groovy\gov\nist\diagnostics
 */

import java.io.InputStream
import java.io.FileInputStream
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.security.spec.X509EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.KeyStore;
import java.security.Security;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.spec.SecretKeySpec
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
 



import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


import java.math.BigInteger;
import java.security.cert.*;
import java.util.*;
import javax.security.auth.x500.X500PrivateCredential;
import java.math.BigInteger;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;

import groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResult


class TaskUtils  {
 def static final UTF8 = 'utf-8'


	def static String getExternalProperty(String fileName, String key) throws Exception {

		
		if (TaskUtils.isNullishString(key)) {
			throw new Exception('Null or invalid key (for a key/value property).')
		}

		try {
			  def props = new Properties()
			  new File(fileName).withInputStream {
				stream -> props.load(stream)
			  }
	  
			  if (null==props[key]) {
				  throw new Exception("The property '$key' was not found in '$fileName'.")
			  }
	  
			  return props[key]
	  
		  } catch (e) {
		  	throw e
		  }
		  return null
	  }
	  
	
	
	def static boolean isFileLocatable(String fileName) throws Exception {
		def File propsFile = new File(fileName)
	
					if (!propsFile.exists())	{
						throw new Exception("File path to '$propsFile.name' as in ($propsFile.absolutePath) could not be resolved.")
					} else {
						return true
					}
	
		return false
	}

	def static boolean isNullishString(String s) {
		
				return ("".equals(s) || s==null || (s!=null && s.isAllWhitespace()))
			
	}
	
	
	def static Certificate getCert(fileName) throws Exception {
		
			 
		try {
		  FileInputStream fis = new FileInputStream(fileName)
		  
		  //org.apache.commons.io.IOUtils.toByteArray(fis)
		  
		  // byte[] ba = IOUtils.toByteArray(fis)
		  
	      CertificateFactory cf = CertificateFactory.getInstance("X.509")
		  Certificate cert = cf.generateCertificate(fis)
		  		 
		  fis.close();
  
		  
		 // X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(ba)
		  // KeyFactory kf = KeyFactory.getInstance("RSA")
		 // PublicKey pk = kf.generatePublic(null) 
		  
	  
		  return cert

		} catch (e) {
			throw e
		}
		return null
	}
	
	
	def static URL[] getLocalClassPathUrls(jarPath) {
		if (!isNullishString(jarPath))
			ClassLoader.systemClassLoader.addURL new File( jarPath ).toURI().toURL()
		return ClassLoader.getSystemClassLoader().getURLs()
	}
		
	
	def static getPublic(byte[] ba) {
		
		//FileInputStream fis = new FileInputStream(fileName) 
		  //byte[] ba = IOUtils.toByteArray(fis)		   
		 X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(ba)
		  KeyFactory kf = KeyFactory.getInstance("RSA")
		 PublicKey pk = kf.generatePublic(pkSpec)
		 return pk.getPublicExponent()

	}			
	
	
	/**
	 * Parts of this method borrowed from existing TTT code.
	 */
	def static Certificate getPrivate(String fileName, String signingCertPwFileName, msgs) throws Exception {

        try {
    		Security.addProvider(new BouncyCastleProvider());
		
        	KeyStore    ks = KeyStore.getInstance("PKCS12", "BC");   
					
			InputStream baIs = new ByteArrayInputStream(FileUtils.readFileToByteArray(new File(fileName)));   		
        	ks.load(baIs, FileUtils.readFileToString(new File(signingCertPwFileName)).toCharArray());
			
	        Enumeration e = ks.aliases()
	        String      keyAlias = null
	
	        while (e.hasMoreElements())
	        {
	            String  alias = (String)e.nextElement()
	
	            if (ks.isKeyEntry(alias))
	            {
	                keyAlias = alias
	            }
	        }
	
	        if (keyAlias == null)
	        {
	            msgs << 'Can\'t find a private key!'
	        } else {
				msgs << "The file is in P12 format."
	        }
			
			return ks.getCertificate(keyAlias)

        } catch (e) {
			msgs << 'Note: A P12 file is expected.'
			msgs << [(ValidationTask.ERROR):e.toString()]
			throw e
        }
	}

	/*
	 * (Grails/DomainValidator)
	 *  * <p>Domain names are evaluated according
	 * to the standards <a href="http://www.ietf.org/rfc/rfc1034.txt">RFC1034</a>,
	 * section 3, and <a href="http://www.ietf.org/rfc/rfc1123.txt">RFC1123</a>,
	 * section 2.1. No accomodation is provided for the specialized needs of
	 * other applications; if the domain name has been URL-encoded, for example,
	 * validation will fail even though the equivalent plaintext version of the
	 * same name would have passed.
	 * </p>
	 * (Seems to work better than other regex in the local jars...)
	 */
	def static boolean isDomainName(final String dn, report) {
		try {
	        def String DOMAIN_LABEL_REGEX = '\\p{Alnum}(?>[\\p{Alnum}-]*\\p{Alnum})*'
	        def String TOP_LABEL_REGEX = '\\p{Alpha}{2,}'
	        def String DOMAIN_NAME_REGEX =  '^(?:' + DOMAIN_LABEL_REGEX + '\\.)+' + '(' + TOP_LABEL_REGEX + ')$'
	
			Pattern pattern = Pattern.compile(DOMAIN_NAME_REGEX, Pattern.CASE_INSENSITIVE);
			
			Matcher matcher = pattern.matcher(dn);
			return (matcher.matches())
			
		} catch (e) {
			report << e.toString()
		}
			
		return false
		
	}
	
	/**
	 * 
	 */
	def static String getCertCnIfValidDomainName(Certificate cert, report) throws Exception {
					def String dn = cert.getSubjectX500Principal().getName()
					def LdapName ldapDN = new LdapName(dn)

					for(Rdn rdn: ldapDN.getRdns()) {
						if ("cn".equals(rdn.getType().toLowerCase())) {
							def String rdnVal = rdn.getValue()?.toString().toLowerCase()
							if (rdnVal!=null) {
								if (TaskUtils.isDomainName(rdnVal, report)) {
									return rdnVal
								}
							}
						}
						report << [(ValidationTask.DEBUG): rdn.getType() + " -> " + rdn.getValue()]
					}
		return null

	}
	

	def static GPathResult getXmlFromFile(String fileName, report) {
		try {
			if (!TaskUtils.isNullishString(fileName)) {
			
				return new XmlSlurper().parse(new File(fileName))
			} else {
				report << [(ValidationTask.ERROR):"fileName appears to be null: ${fileName}."]
			}
	
		} catch (e) {
			report << [(ValidationTask.ERROR):e.toString()]
		}

	}
	
}

