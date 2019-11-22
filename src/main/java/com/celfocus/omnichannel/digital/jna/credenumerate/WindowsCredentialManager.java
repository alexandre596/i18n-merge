package com.celfocus.omnichannel.digital.jna.credenumerate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

@Component
public class WindowsCredentialManager {

	public Map<String, GenericWindowsCredentials> genericCredentials; 	
	
	public WindowsCredentialManager() {
		genericCredentials = new HashMap<>();
		enumerateGenericCredentials();
	}
	
	public GenericWindowsCredentials getByTargetName(String targetName) {
		return genericCredentials.get(targetName);
	}
	
	private void enumerateGenericCredentials() {
		IntByReference pCount = new IntByReference();
		PointerByReference pCredentials = new PointerByReference();	
		
		Advapi32_Credentials.INSTANCE.CredEnumerateW(null, 0, pCount, pCredentials);
		Pointer[] ps = pCredentials.getValue().getPointerArray(0,  pCount.getValue());
		
		for (int i=0; i<pCount.getValue(); i++) {
			Credential arrRef = new Credential(ps[i]);
			arrRef.read();
			if (CredentialType.valueOf(arrRef.Type) == CredentialType.CRED_TYPE_GENERIC) { //only generic credentials
			
				GenericWindowsCredentials gwc = new GenericWindowsCredentials();
				gwc.setAddress(arrRef.TargetName.getWideString(0)); //address
				gwc.setUsername(getUserName(arrRef)); //username
				
			if (arrRef.CredentialBlobSize > 0) {
				byte[] bytes = arrRef.CredentialBlob.getByteArray(0, arrRef.CredentialBlobSize);
				
				gwc.setPassword(new String(bytes, StandardCharsets.UTF_16LE)); //password
			}		
					
			genericCredentials.put(gwc.getAddress(), gwc);
		
			}
		}
	}

	private String getUserName(Credential arrRef) {
		String result = null;
		try {				
			if (arrRef.UserName != null) {
				result = arrRef.UserName.getWideString(0); 
			}
		} catch (java.lang.Error e) {
			System.out.println("ERROR: " + e.getMessage());
		} 
		return result;
	}
		
}
