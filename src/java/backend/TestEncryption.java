
public class TestEncryption {
    public static void main(String[] args) {
        String str = "university of santo tomas";
        
        System.out.println("Original Str: " + str);
        
        String encrypStr = Security.encrypt(str);
        
        System.out.println("Encrypted version: " + encrypStr);
        
        System.out.println("Decrypted version: " + Security.decrypt(encrypStr));
    }
}


/*

       private static byte[] key = {'l','a','w','r','e','n','c','e',
                                     'd','e','c','a','m','o','r','a'};

Original Str: university of santo tomas
Encrypted version: +dcrgAbzQxC16Oogbp1W4efGxGjVyfgTWzx/AXWK5Uo=
Decrypted version: university of santo tomas
*/



/*

	private static byte[] key = {
	 	0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 
                0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
                 // thisisasecretkey
	};

Original Str: university of santo tomas
Encrypted version: X03O/bZKNujj5EHOnGB41TLCWUWlvxtbMA9dViDNg9o=
Decrypted version: university of santo tomas


*/