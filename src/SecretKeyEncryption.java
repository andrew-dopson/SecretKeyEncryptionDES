/*Andrew Dopson
 * login id: adopson
 * WVU Student ID: 701017103
 * Programming Assignment 1
 * 09/22/2014
 * 
 * This program fully compiles.
 * 
 * The only aspect that I do not believe works correctly is the decryption
 * algorithm. It still produces an output, but I do not believe that what
 * it outputs is correct.
 * 
 */



import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class SecretKeyEncryption {
	
	static ArrayList<char[]> keySchedule = new ArrayList<char[]>();
	static ArrayList<int[][]> sBoxes = new ArrayList<int[][]>();
	
	static int[][] s1 = {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
				  {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
				  {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
				  {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};
	
	static int[][] s2 = {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
				  {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
				  {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
				  {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};
	
	static int[][] s3 = {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
			      {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
			      {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
			      {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};
	
	static int[][] s4 = {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
			      {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
			      {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
			      {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};
	
	static int[][] s5 = {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
			      {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
			      {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
			      {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};
	
	static int[][] s6 = {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
				  {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
				  {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
				  {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};
	
	static int[][] s7 = {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
			  	  {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
			  	  {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
			  	  {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};
	
	static int[][] s8 = {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
			      {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
			      {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
			      {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

	
	public static void main(String[] args) {
		boolean fileExists = false;
		String textString = "";
		String binaryString = null;
		Scanner input = new Scanner(System.in);
		String key = "";
		sBoxes.add(s1);
		sBoxes.add(s2);
		sBoxes.add(s3);
		sBoxes.add(s4);
		sBoxes.add(s5);
		sBoxes.add(s6);
		sBoxes.add(s7);
		sBoxes.add(s8);
		
		//Enter the path to the test.txt file in src folder
		File fileName = new File("~/SecretKeyEncryptionDES/src/test.txt");
		//method that converts the given file into a complete string
		textString = fileToString(fileName);
		//method that converts the string above into a binary string of 64 bit blocks
		binaryString = textToBinary(textString);
		
		//loop to receive key from user input
		while(key.length() != 8){
			System.out.print("Enter the 8 character key you would like to use: ");
			key = input.next();
			
			if(key.length() != 8){
				System.out.println("Key must be 8 characters long. Try again.");
			}
		}
		System.out.println("Text to encrypt: " + textString);
		System.out.println("\nBinary text: \n" + binaryString);
		System.out.println("\nPassword: " + key);
		//Converts key to binary
		key = keyToBinary(key);
		createKeySchedule(key);

		//shows all key values
		for(int i = 0; i < 16; i++){
			System.out.printf("Key %d: \n\t", (i+1));
			System.out.println(keySchedule.get(i));
		}
		
		StringBuilder encrypted = new StringBuilder();
		
		//For each 64-bit block, the DES encryption method is called.
		String[] data = binaryString.split("\\n\\n");
		for(int i = 0; i < data.length; i++){
			data[i] = data[i].replaceAll("\\n", "");
			encrypted.append(DES(data[i]));
			encrypted.append("\n");
		}
		System.out.print("\nFULLY ENCRYPTED TEXT: \n");
		System.out.println(encrypted.toString().toCharArray());
		
		decipher(encrypted.toString());
	}
	
	/*This method takes the encrypted text and does
	 *a reverse algorithm to decrypt the text back to 
	 *its original message. 
	 */
	private static void decipher(String string) {
		String[] data = string.split("\\n");
		StringBuilder decrypted = new StringBuilder();
		for(int j = 0; j < data.length; j++){
			char[] message = data[j].toCharArray();
			message = inverseP(message);
			
			String text = new String(message);
			char[] Lminus1 = text.substring(0, 32).toCharArray();
			char[] Rminus1 = text.substring(32, 64).toCharArray();
			char[] Ln = null;
			char[] Rn = null;
			
			for(int i = 15; i >= 0; i--){
				Ln = Rminus1;
				Rminus1 = ePerm(Rminus1);
				Rminus1 = add(Rminus1, keySchedule.get(i));
				Rminus1 = sBox(Rminus1);
				Rminus1 = permute(Rminus1);
				Lminus1 = add(Lminus1, Rminus1);
				
				if(i != 0){
					Rminus1 = Lminus1;
					Lminus1 = Ln;
				}
			}
			Rminus1 = Ln;
			StringBuilder finalPerm = new StringBuilder(64);
			finalPerm.append(Lminus1);
			finalPerm.append(Rminus1);
			char[] last = initialP(finalPerm.toString().toCharArray());
			decrypted.append(last);
			decrypted.append("\n");
		}
		System.out.println("\nFULLY DECRYPTED TEXT: \n");
		System.out.println(decrypted.toString());
		
	}

	/*This method applies the DES algorithm to each block
	 * of 64 bit data.
	 */
	private static char[] DES(String data) {
		char[] message = data.toCharArray();
		message = initialP(message);
		System.out.print("\nInitial Permutation: \n\t");
		System.out.println(message);
		String text = new String(message);
		char[] Lminus1 = text.substring(0, 32).toCharArray();
		char[] Rminus1 = text.substring(32, 64).toCharArray();
		char[] Ln = null;
		char[] Rn = null;
		
		//main DES cycle
		for(int i = 0; i < 16; i++){
			System.out.println("\nIteration: " + (i+1));
			System.out.print("\nL_n-1:\n\t");
			System.out.println(Lminus1);
			System.out.print("R_n-1:\n\t");
			System.out.println(Rminus1);
			System.out.print("\nExpansion Permutation: \n\t");
			
			Ln = Rminus1;
			//permutates R from 32 to 48 bits
			Rminus1 = ePerm(Rminus1);
			
			System.out.println(Rminus1);
			System.out.print("\nXOR with key: \n\t");
			
			//Bitwise addition with R and the key for the current cycle
			Rminus1 = add(Rminus1, keySchedule.get(i));
	
			System.out.println(Rminus1);
			System.out.print("\nS-box substitution: \n\t");
			
			//S-Box substitution
			Rminus1 = sBox(Rminus1);
			
			System.out.println(Rminus1);
			System.out.print("\nP-box permutation: \n\t");
			
			//Permutes result from S-box
			Rminus1 = permute(Rminus1);
			
			System.out.println(Rminus1);
			System.out.print("\nXOR with L_n-1 (R_n): \n\t");
			
			//Bit wise addition of L and R
			Lminus1 = add(Lminus1, Rminus1);
			
			System.out.println(Lminus1);
			System.out.printf("\nEnd of iteration: %d \n", (i+1));
			if(i != 15){
				Rminus1 = Lminus1;
				Lminus1 = Ln;
			}
		}
		Rminus1 = Ln;
		
		StringBuilder finalPerm = new StringBuilder(64);
		finalPerm.append(Lminus1);
		finalPerm.append(Rminus1);
		char[] last = inverseP(finalPerm.toString().toCharArray());
		
		System.out.print("\nFinal Permutation: \n\t");
		System.out.println(last);
		System.out.println("\n");
		
		return last;
	}
	
	/*This method is used to create the s-box representation
	 * of R for each cycle. Each group of 6 bits is used to find
	 * the value for their respective primitive function of S.
	 */
	private static char[] sBox(char[] r) {
		int row = 0;
		int column = 0;
		int sNum = 0;
		
		
		for(int i = 1; i <= 8; i++){
			row = 0;
			column = 0;
			
			if(r[(i*6) - 6] == '1'){row += 2;}
			if(r[(i*6) - 5] == '1'){column += 8;}
			if(r[(i*6) - 4] == '1'){column += 4;}
			if(r[(i*6) - 3] == '1'){column += 2;}
			if(r[(i*6) - 2] == '1'){column += 1;}
			if(r[(i*6) - 1] == '1'){row += 1;}
			
			sNum = sBoxes.get(i - 1)[row][column];
			
			if((sNum / 8) == 1){
				r[i*4-4] = '1';
			}else{r[i*4-4] = '0';}
			sNum = sNum % 8;
			if((sNum / 4) == 1){
				r[i*4-3] = '1';
			}else{r[i*4-3] = '0';}
			sNum = sNum % 4;
			if((sNum / 2) == 1){
				r[i*4-2] = '1';
			}else{r[i*4-2] = '0';}
			sNum = sNum % 2;
			if(sNum == 1){
				r[i*4-1] = '1';
			}else{r[i*4-1] = '0';}	
		}
		return r;
	}

	/*Instead of dealing with addition, this method completes
	 * the same task, but just compares if each character is equal 
	 * to its respective character in the key. If they are equal, then
	 * the character are either 1 and 1, or 0 and 0, in which case their
	 * modulo two addition will equal 0. If they are opposite, then one character
	 * is 1 and the other is 0, in which the modulo 2 addition will equal 1 in 
	 * either case.
	 */
	private static char[] add(char[] r, char[] key) {
		for(int i = 0; i < r.length; i++){
			if(r[i] != key[i]){
				r[i] = '1';
			}else{
				r[i] = '0';
			}
		}
		return r;
	}

	/*This method applies the initial permutation to the 64 bit 
	 * block being enciphered.
	 */
	private static char[] initialP(char[] message) {
		char[] messageHolder = {message[57], message[49], message[41], message[33], message[25], message[17], message[9], message[1],
				message[59], message[51], message[43], message[35], message[27], message[19], message[11], message[3],
				message[61], message[53], message[45], message[37], message[29], message[21], message[13], message[5],
				message[63], message[55], message[47], message[39], message[31], message[23], message[15], message[7],
				message[56], message[48], message[40], message[32], message[24], message[16], message[8], message[0],
				message[58], message[50], message[42], message[34], message[26], message[18], message[10], message[2],
				message[60], message[52], message[44], message[36], message[28], message[20], message[12], message[4],
				message[62], message[54], message[46], message[38], message[30], message[22], message[14], message[6]};
		
		return messageHolder;
	}
	
	/*This method applies the inverse permutation to the 64 bit 
	 * preoutput.
	 */
	private static char[] inverseP(char[] message) {
		char[] messageHolder = {message[39], message[7], message[47], message[15], message[55], message[23], message[63], message[31],
				message[38], message[6], message[46], message[14], message[54], message[22], message[62], message[30],
				message[37], message[5], message[45], message[13], message[53], message[21], message[61], message[29],
				message[36], message[4], message[44], message[12], message[52], message[20], message[60], message[28],
				message[35], message[3], message[43], message[11], message[51], message[19], message[59], message[27],
				message[34], message[2], message[42], message[10], message[50], message[18], message[58], message[26],
				message[33], message[1], message[41], message[9], message[49], message[17], message[57], message[25],
				message[32], message[0], message[40], message[8], message[48], message[16], message[56], message[24]};
		
		return messageHolder;
	}

	/*This method provides the E(R) function for each cipher function
	 * 
	 */
	private static char[] ePerm(char[] message){
		char[] holder = {message[31], message[0], message[1], message[2], message[3], message[4],
				message[3], message[4], message[5], message[6], message[7], message[8],
				message[7], message[8], message[9], message[10], message[11], message[12],
				message[11], message[12], message[13], message[14], message[15], message[16],
				message[15], message[16], message[17], message[18], message[19], message[20],
				message[19], message[20], message[21], message[22], message[23], message[24],
				message[23], message[24], message[25], message[26], message[27], message[28],
				message[27], message[28], message[29], message[30], message[31], message[0]};
				
		return holder;
	}
	
	/*This method applies the permutation function in each cycle.
	 * It yields a 32-bit output from a 32-bit input.
	 */
	private static char[] permute(char[] message){
		char[] holder = {message[15], message[6], message[19], message[20],
				message[28], message[11], message[27], message[16],
				message[0], message[14], message[22], message[25],
				message[4], message[17], message[30], message[9],
				message[1], message[7], message[23], message[13],
				message[31], message[26], message[2], message[8],
				message[18], message[12], message[29], message[5],
				message[21], message[10], message[3], message[24]};
		
		return holder;
	}
		
	/*This method converts the given password from its ASCII value
	 * to binary in the same way the method 'textToBinary' converts  
	 * the given text.
	 */
	private static String keyToBinary(String key) {
		byte[] bytes = key.getBytes();
		StringBuilder binaryString = new StringBuilder();
		
		for (int i = 0; i < bytes.length; i++){
			int character = bytes[i];
			for(int j = 0; j < 8; j++){
				binaryString.append((character & 128) == 0 ? 0 : 1);
				character <<= 1;
			}
		}
		return binaryString.toString();
	}

	/*This method creates 16 keys from the given password
	 * from user input using the key scheduling algorithm.
	 */
	public static void createKeySchedule(String key) {
		char[] keyArray = key.toCharArray();
		
		/*"This method below is called to shift initial binary string to move 
		 * most significant bits to the least significant. These will then
		 * be used as parity bits.
		 */
		keyArray = leftShift64(keyArray);
		//sets parity bits of the key
		keyArray = parity(keyArray);
		System.out.print("\nKey before permute and after parity: ");
		System.out.println(keyArray);
		keyArray = pc1(keyArray);
		System.out.print("\nKey after permute: ");
		System.out.println(keyArray);
		
		
		for(int i = 1; i < 17; i++){
			if (i == 1 || i == 2 || i == 9 || i == 16){
				keyArray = leftShift56(keyArray);
				keySchedule.add(pc2(keyArray));
			}else{
				keyArray = leftShift56(keyArray);
				keyArray = leftShift56(keyArray);	
				keySchedule.add(pc2(keyArray));
				
			}
		}
	}

	/*This method is used to shift the C_n and D_n of each iteration
	 * that has 56 total bits. The array is shifted by 1 bit to the left.
	 */
	private static char[] leftShift56(char[] keyArray) {
		char[] c = null;
		char[] d = null;
		String key = new String(keyArray);
		c = key.substring(0, 28).toCharArray();
		d = key.substring(28).toCharArray();
		char cHolder = c[0];
		char dHolder = d[0];
		
		for(int i = 0; i < 27; i++){
			c[i] = c[i+1];
			d[i] = d[i+1];
		}
		c[27] = cHolder;
		d[27] = dHolder;
		
		String cc = new String(c);
		String dd = new String(d);
		key = cc.concat(dd);
		return key.toCharArray();
	}

	/*This method runs the key through the Permuted Choice 1 table and
	 *changes the key according to the table. 
	 */
	public static char[] pc1(char[] keyArray) {
		char[] keyHolder = {keyArray[56], keyArray[48], keyArray[40], keyArray[32], keyArray[24], keyArray[16], keyArray[8],
		                    keyArray[0], keyArray[57], keyArray[49], keyArray[41], keyArray[33], keyArray[25], keyArray[17],
		                    keyArray[9], keyArray[1], keyArray[58], keyArray[50], keyArray[42], keyArray[34], keyArray[26],
		                    keyArray[18], keyArray[10], keyArray[2], keyArray[59], keyArray[51], keyArray[43], keyArray[35],
		                    keyArray[62], keyArray[54], keyArray[46], keyArray[38], keyArray[30], keyArray[22], keyArray[14],
		                    keyArray[6], keyArray[61], keyArray[53], keyArray[45], keyArray[37], keyArray[29], keyArray[21],
		                    keyArray[13], keyArray[5], keyArray[60], keyArray[52], keyArray[44], keyArray[36], keyArray[28],
		                    keyArray[20], keyArray[12], keyArray[4], keyArray[27], keyArray[19], keyArray[11], keyArray[4]};
		
		keyArray = keyHolder;
		return keyArray;
	}
	
	/*This method runs the key through the Permuted Choice 2 table and
	 *changes the key according to the table.
	 */
	private static char[] pc2(char[] keyArray) {
		char[] keyHolder = {keyArray[13], keyArray[16], keyArray[10], keyArray[23], keyArray[0], keyArray[4], 
                			keyArray[2], keyArray[27], keyArray[14], keyArray[5], keyArray[20], keyArray[9], 
                			keyArray[22], keyArray[18], keyArray[11], keyArray[3], keyArray[25], keyArray[7], 
                			keyArray[15], keyArray[6], keyArray[26], keyArray[19], keyArray[12], keyArray[1], 
                			keyArray[40], keyArray[51], keyArray[30], keyArray[36], keyArray[46], keyArray[54], 
                			keyArray[29], keyArray[39], keyArray[50], keyArray[44], keyArray[32], keyArray[47], 
                			keyArray[43], keyArray[48], keyArray[38], keyArray[55], keyArray[33], keyArray[52], 
                			keyArray[45], keyArray[41], keyArray[49], keyArray[35], keyArray[28], keyArray[31]};

		keyArray = keyHolder;
		return keyArray;
	}
	
	//Method that creates odd parity bits in each character's most significant bit
	private static char[] parity(char[] keyArray) {
		for (int i = 1; i < 9; i++){
			if ((((int)keyArray[i*8 - 8] + (int)keyArray[i*8 - 7] + (int)keyArray[i*8 - 6] + (int)keyArray[i*8 - 5] + (int)keyArray[i*8 - 4] + (int)keyArray[i*8 - 3] + (int)keyArray[i*8 - 2]) % 2) == 0){
				keyArray[i*8 - 1] = '1';		
			}else{
				keyArray[i*8 - 1] = '0';
			}
		}
		return keyArray;
	}
	
	/*Shifts all bits in the array to the left by one. Most significant
	 * bit becomes the least significant.
	 */
	private static char[] leftShift64(char[] keyArray) {
		char holder = keyArray[0];
		for(int i = 0; i < 63; i++){
			keyArray[i] = keyArray[i+1];
		}
		keyArray[63] = holder;
		return keyArray;
	}

	
	/*This method creates a buffer that reads each line from the input file 
	  and concatenates it with the String 'text'.  Also catches any IO exceptions
	  might occur and closes the buffer after the file has been read. The string
	  is returned to main.
	  */
	private static String fileToString(File file){
		BufferedReader reader = null;
		String text = "";
		try{
			String newLine;
			reader = new BufferedReader(new FileReader(file));
			while((newLine = reader.readLine()) != null){
				text += newLine;
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(reader != null) reader.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		text = text.replaceAll("\\W", "");
		return text;
	}
	
	
	/*This method takes in the string created from fileToString. All non alphanumeric
	 * characters are omitted from the string here, with the remaining string converted to 
	 * a byte array. Once the byte array is created, the for loop iterates through each individual 
	 * byte and converts the ascii character to its binary representation. Each binary character 
	 * is put on its own line, and every 8 characters are separated by a blank line, which puts
	 * the string into 64 bit blocks. Zeros are added to the last block if there are less than 64 bits.
	 */
	private static String textToBinary(String text){
		byte[] bytes = text.getBytes();
		StringBuilder binaryString = new StringBuilder();
		int counter = 0;
		
		for (int i = 0; i < bytes.length; i++){
			int character = bytes[i];
			for(int j = 0; j < 8; j++){
				binaryString.append((character & 128) == 0 ? 0 : 1);
				character <<= 1;
			}
			binaryString.append('\n');
			counter++;
			
			if ((counter % 8) == 0){
				binaryString.append('\n');
			}
			
		}
		int addedZeros = 8 - (counter % 8);
		for (int k = 0; k < addedZeros; k++){
			binaryString.append("00000000\n");
		}
		
		return binaryString.toString();
	}
}

