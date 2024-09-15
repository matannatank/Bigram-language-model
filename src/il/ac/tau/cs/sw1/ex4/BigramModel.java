package il.ac.tau.cs.sw1.ex4;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class BigramModel {
	public static final int MAX_VOCABULARY_SIZE = 14500;
	public static final String VOC_FILE_SUFFIX = ".voc";
	public static final String COUNTS_FILE_SUFFIX = ".counts";
	public static final String SOME_NUM = "some_num";
	public static final int ELEMENT_NOT_FOUND = -1;
	
	String[] mVocabulary;
	int[][] mBigramCounts;
	
	// DO NOT CHANGE THIS !!! 
	public void initModel(String fileName) throws IOException{
		mVocabulary = buildVocabularyIndex(fileName);
		mBigramCounts = buildCountsArray(fileName, mVocabulary);
		
	}
	
	
	
	private void printing() {
	    //System.out.println(Arrays.toString(mVocabulary));
	}
	
	
	
	private boolean isNotEmpty(String word) {
	    // Add any additional conditions for considering a word as not empty
	    return !word.isEmpty();
	}
	
	
	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 */
	public String[] buildVocabularyIndex(String fileName) throws IOException {
	    String[] vocabulary = new String[MAX_VOCABULARY_SIZE];
	    String file = fileName;
	    BufferedReader br = new BufferedReader(new FileReader(file));

	    int i = 0;
	    String line;

	    while ((line = br.readLine()) != null && i < MAX_VOCABULARY_SIZE) {
	        String[] words = line.split("\\s+");

	        for (String word : words) {
	            String legalWord = legalWordOrNumber(word);

	            if (legalWord != null && !isInVocabulary(legalWord, vocabulary)) {
	            	
	                vocabulary[i] = legalWord;
	              
	                i++;
	            }
	        }
	    }

	    //System.out.println(Arrays.toString(Arrays.copyOf(vocabulary, i)));
	  //System.out.println(Arrays.toString(Arrays.copyOfRange(vocabulary, 0, i)));

	    // Trim the vocabulary array to remove null entries
	    return Arrays.copyOf(vocabulary, i);
	}

        
        
    

    private boolean isInVocabulary(String word, String[] vocabulary) {
        for (String vocabWord : vocabulary) {
            if (word.equalsIgnoreCase(vocabWord)) {
                return true;
            }
        }
        return false;
    }
    
    
    private void addToVocabularyIfNotExists(String word, String[] vocabulary) {
        if (!isInVocabulary(word, vocabulary)) {
            // The word is not in the vocabulary, so add it
            for (int i = 0; i < vocabulary.length; i++) {
                if (vocabulary[i] == null || !vocabulary[i].equalsIgnoreCase(word)) {
                    // Found an empty slot in the vocabulary or the word is not present, add the word
                    vocabulary[i] = word;
                    break;
                }
            }
        } 
    }

    public String legalWordOrNumber(String word) {
        // Check if it's a legal number
        if (legalInt(word)) {
            return SOME_NUM;
        }

        // Check if it's a legal word
        String legalWord = legalWord(word);
        if (legalWord != null) {
            return legalWord;
        }

        // Check if the word contains at least one English letter
        boolean containsEnglishLetter = false;
        boolean containsNonEnglishLetter = false;

        for (char ch : word.toCharArray()) {
            if (Character.isLetter(ch)) {
                if (Character.UnicodeScript.of(ch) == Character.UnicodeScript.LATIN) {
                    containsEnglishLetter = true;
                } else {
                    containsNonEnglishLetter = true;
                }
            }
        }

        // If it contains at least one English letter and no non-English letters, consider it legal
        if (containsEnglishLetter && !containsNonEnglishLetter) {
            return word;
        }

        // If the word contains at least one non-English letter, consider it not legal
        return null;
    }


    
    
     // Helper method to check if a string contains a digit
        private boolean containsDigit(String s) {
            for (char ch : s.toCharArray()) {
                if (Character.isDigit(ch)) {
                    return true;
                }
            }
            return false;
        }

        // Helper method to check if a string contains a letter (assuming Latin script)
        private boolean containsLetter(String s) {
            for (char ch : s.toCharArray()) {
                if (Character.isLetter(ch) && Character.UnicodeScript.of(ch) == Character.UnicodeScript.LATIN) {
                    return true;
                }
            }
            return false;
        }
    
    private boolean legalInt(String word) {
        // Check if the word is empty or null
        if (word == null || word.isEmpty()) {
            return false;
        }

        // Check if the first character is a digit
        char firstChar = word.charAt(0);
        if (!Character.isDigit(firstChar)) {
            return false;
        }

        // Check if the remaining characters are digits
        for (int i = 1; i < word.length(); i++) {
            if (!Character.isDigit(word.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    
    
    private String legalWord(String word) {
        boolean containsEnglishLetter = false;
        for (char ch : word.toCharArray()) {
            if (Character.isLetter(ch) && Character.UnicodeScript.of(ch) == Character.UnicodeScript.LATIN) {
                containsEnglishLetter = true;
                break;
            }
        }
        if (containsEnglishLetter) {
            return word.toLowerCase();
        } else {
            return null; // or throw an exception, depending on your logic
        }
    }

	
	   
	   
	   
	   
	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 * 
	 * want to fix this method
	 
    public int[][] buildCountsArray(String fileName, String[] vocabulary) throws IOException {
        int maxRowLength = 0;
        int totalRows = 0;

        int[][] bigramCounts = new int[vocabulary.length][vocabulary.length];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                totalRows++;

                int lineLength = line.length();
                if (lineLength > maxRowLength) {
                    maxRowLength = lineLength;
                }

                for (int t = 0; t < words.length - 1; t++) {
                    String word1 = words[t].toLowerCase();
                    String word2 = words[t + 1].toLowerCase();

                    int index1 = getWordIndex(word1, vocabulary);
                    int index2 = getWordIndex(word2, vocabulary);

                    if (index1 != ELEMENT_NOT_FOUND && index2 != ELEMENT_NOT_FOUND) {
                        bigramCounts[index1][index2]++;
                    }
                }
            }
        }

        // Print the matrix
    //    printMatrix(bigramCounts);
        
        //System.out.println("printing mvocubolary ;");
        //System.out.println(Arrays.toString(mVocabulary));
        //System.out.println("printing mvocubolary ;");

        
        return bigramCounts;
    }

    */
    
	
    public int[][] buildCountsArray(String fileName, String[] vocabulary) throws IOException {
        int maxRowLength = 0;
        int totalRows = 0;

        int[][] bigramCounts = new int[vocabulary.length][vocabulary.length];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                totalRows++;

                int lineLength = line.length();
                if (lineLength > maxRowLength) {
                    maxRowLength = lineLength;
                }

                for (int t = 0; t < words.length - 1; t++) {
                	
                    String word1 = words[t].toLowerCase();
                    String word2 = words[t + 1].toLowerCase();
                    
                 // Check if word1 is a legal integer
                    if (legalInt(word1)) {
                        word1 = SOME_NUM;
                    }

                    // Check if word2 is a legal integer
                    if (legalInt(word2)) {
                        word2 = SOME_NUM;
                        
                    }
                    int index1 = getWordIndex(word1, vocabulary);
                    int index2 = getWordIndex(word2, vocabulary);

                    if (index1 != ELEMENT_NOT_FOUND && index2 != ELEMENT_NOT_FOUND) {
                        bigramCounts[index1][index2]++;
                    }
                }
            }
        }

        // Print the matrix
    //    printMatrix(bigramCounts);
        
        //System.out.println("printing mvocubolary ;");
        //System.out.println(Arrays.toString(mVocabulary));
        //System.out.println("printing mvocubolary ;");

        
        return bigramCounts;
    }

    
    
    
	public int[][] printMatrix(int[][] bigramCounts) {
	    // Print the matrix
		int num = 0;
		while(num<2) {
		
	    for (int[] row : bigramCounts) {
	        for (int count : row) {
	            //System.out.print(count + " ");
	        }
	        //System.out.println();
	        num++ ;
	    }
		}
	    return bigramCounts;
	}

	
	
	
	
	    private int getWordIndex(String word, String[] vocabulary) {
	        for (int i = 0; i < vocabulary.length; i++) {
	            if (word.equalsIgnoreCase(vocabulary[i])) {
	                return i;
	            }
	        }
	        return ELEMENT_NOT_FOUND;
	    }

	    
	    
	    
	    
	
	/*
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: fileName is a legal file path
	 */
	public void saveModel(String fileName) throws IOException{// Q-3
		// add your code here
		
        String vocFileName = fileName + VOC_FILE_SUFFIX;
	
        // Open the file for writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(vocFileName))) {
            
            // Write the number of words to the file
            writer.write(mVocabulary.length + " words");
            writer.newLine();

            // Write each word and its index to the file
            for (int i = 0; i < mVocabulary.length; i++) {
                writer.write(i + "," + mVocabulary[i]);
               // //System.out.println(i + "," + mVocabulary[i]);
                writer.newLine();
            }
                
        } 
        
        // now we will write the filename.counts, write the vocabulary and the index of the words
       
        String countsfilename = fileName + COUNTS_FILE_SUFFIX;

        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(countsfilename))) {
            for (int i = 0; i < mBigramCounts.length; i++) {
                for (int j = 0; j < mBigramCounts[i].length; j++) {
                    int count = mBigramCounts[i][j];
                    if (count != 0) {
                        bw.write(i + "," + j + ":" + count);
                        bw.newLine(); // move to the next line
                    }
                }
                
                
            }
        }
        
            
      }
		
		
		
	
	/*
	 * @pre: fileName is a legal file path
	 */
	
	
	// here is the problem
	public void loadModel(String fileName) throws IOException{ // Q - 4
        // Load vocabulary from voc.fileName
		
        //System.out.println("printing mvoc  first load model :");
        //System.out.println(Arrays.toString(mVocabulary));
        //System.out.println("printing mvoc first load model :");
		
		
        String vocabFileName = fileName + VOC_FILE_SUFFIX;
        mVocabulary = loadVocabulary(vocabFileName);
        
        int len_voc = mVocabulary.length;

        // Load counts from counts.fileName 
        String countsFileName = fileName + COUNTS_FILE_SUFFIX;
        mBigramCounts = loadCounts(countsFileName,len_voc);
        
        
        //System.out.println("printing mvoc second load model :");
        //System.out.println(Arrays.toString(mVocabulary));
        //System.out.println("printing mvoc second load model :");

        printModel();

    }

	
	
	// the problem is here ! , the load vocabulary suppose to upload
	// the word to the mvocabulary and overwrite the data at mvocabulary
	
	private String[] loadVocabulary(String fileName) throws IOException {
	    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	        // Read the first line to get the number of words
	        int numWords = Integer.parseInt(br.readLine().split(" ")[0]);

	        String[] vocabulary = new String[numWords];

	        // Read each subsequent line to get index and word
	        String line;
	        while ((line = br.readLine()) != null) {
	            // Split each line into index and word at the first comma
	            String[] parts = line.split(",", 2);
	            int index = Integer.parseInt(parts[0]);
	            String word = parts[1];

	            // If the word contains commas, reconstruct it by joining all parts
	            if (parts.length > 2) {
	                for (int i = 2; i < parts.length; i++) {
	                    word += "," + parts[i];
	                }
	            }

	            vocabulary[index] = word;
	        }

	        return vocabulary;
	    }
	}



    private int[][] loadCounts(String fileName, int len_voc) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int[][] bigramCounts = new int[len_voc][len_voc];

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String[] indices = parts[0].split(",");
                int i = Integer.parseInt(indices[0]);
                int j = Integer.parseInt(indices[1]);
                int count = Integer.parseInt(parts[1]);

                bigramCounts[i][j] = count;
            }

            return bigramCounts;
        }
    }

    
    
    
    public void printModel() {
        //System.out.println("Vocabulary:");
        //System.out.println(Arrays.toString(mVocabulary));

        //System.out.println("\nBigram Counts:");
        for (int[] row : mBigramCounts) {
            for (int count : row) {
                //System.out.print(count + " ");
            }
            //System.out.println();
        }
    }


	
	
	/*
	 * @pre: word is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post: $ret = -1 if word is not in vocabulary, otherwise $ret = the index of word in vocabulary
	 */
	public int getWordIndex(String word){  // Q - 5
		  for (int i = 0; i < mVocabulary.length; i++) {
		        if (mVocabulary[i].equals(word)) {
		            return i;
		        }
		    }
		    return ELEMENT_NOT_FOUND; // assuming ELEMENT_NOT_FOUND is a constant defined in your class
		}
	
	
	
	/*
	 * @pre: word1, word2 are in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post: $ret = the count for the bigram <word1, word2>. if one of the words does not
	 * exist in the vocabulary, $ret = 0
	 */
	public int getBigramCount(String word1, String word2){ //  Q - 6
		// replace with your code
		
	    int index1 = getWordIndex(word1);
	    int index2 = getWordIndex(word2);
	    
        //System.out.println(Arrays.toString(mVocabulary));

	    // Check if both words are in the vocabulary
	    if (index1 != ELEMENT_NOT_FOUND && index2 != ELEMENT_NOT_FOUND) {
	        //System.out.println(mBigramCounts[index1][index2]);
	        return mBigramCounts[index1][index2];
	    }

	    // One or both words are not in the vocabulary
	    return 0;
	}
	
	
	
	
	/*
	 * @pre word in lowercase, and is in mVocabulary
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post $ret = the word with the lowest vocabulary index that appears most fequently after word (if a bigram starting with
	 * word was never seen, $ret will be null
	 */
	public String getMostFrequentProceeding(String word){ //  Q - 7
		// replace with your code
	    int wordIndex = getWordIndex(word);

	    if (wordIndex == ELEMENT_NOT_FOUND) {
	        return null; // Word not in vocabulary
	    }

	    int maxCount = 0;
	    int maxIndex = -1;

	    for (int i = 0; i < mVocabulary.length; i++) {
	        if (mBigramCounts[wordIndex][i] > maxCount) {
	            maxCount = mBigramCounts[wordIndex][i];
	            maxIndex = i;
	        } else if (mBigramCounts[wordIndex][i] == maxCount && maxIndex == -1) {
	            // If there are multiple words with the same count, choose the one with the lower index
	            maxIndex = i;
	        }
	    }

	    if (maxIndex != -1) {
	        return mVocabulary[maxIndex];
	    } else {
	        return null; // No proceeding word found
	    }
	}
	
	
	
	/* @pre: sentence is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: each two words in the sentence are are separated with a single space
	 * @post: if sentence is is probable, according to the model, $ret = true, else, $ret = false
	 */
	
	
	public boolean isLegalSentence(String sentence){  //  Q - 8
		// replace with your code
        //System.out.println(sentence);

		  // If the sentence is empty or contains only one word found in the dictionary, return true
	    if (sentence.trim().isEmpty() || sentence.split("\\s+").length == 1) {
	        return true;
	    }

	    String[] words = sentence.split("\\s+");
	   // //System.out.println(Arrays.toString(words));
		
	   // //System.out.println("words.length : " +words.length);

	    for (int i = 0; i < words.length-1 ; i++) {
	        //System.out.println(i);

	        String word1 = convertToLegalWord(words[i]);
	        String word2 = convertToLegalWord(words[i + 1]);
	        ///
	        //System.out.println(word1 + "   "+word2);
	        ///
	        int index1 = getWordIndex(word1);
	        int index2 = getWordIndex(word2);
	        //System.out.println("word1 :  "+ word1 + "   "+index1);
	        //System.out.println("word2 :  " + word2 + "   "+index2);

	        // If either word is not in the vocabulary or the bigram count is zero, return false
	        if (index1 == ELEMENT_NOT_FOUND || index2 == ELEMENT_NOT_FOUND || mBigramCounts[index1][index2] == 0 ) {
	            return false;
	        }
	    }

	    // All adjacent word pairs in the sentence have occurred in the training text
	    return true;
	}
	
	
	

	private String convertToLegalWord(String word) {
	    if (isNumeric(word)) {
	        return SOME_NUM;
	    } else {
	        return word.toLowerCase();
	    }
	}

	private boolean isNumeric(String str) {
	    return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	
	
	
	
	
	/*
	 * @pre: arr1.length = arr2.legnth
	 * post if arr1 or arr2 are only filled with zeros, $ret = -1, otherwise calcluates CosineSim
	 */
	public static double calcCosineSim(int[] arr1, int[] arr2){ //  Q - 9
		// replace with your code
		 // Calculate the dot product
	    double dotProduct = 0;
	    for (int i = 0; i < arr1.length; i++) {
	        dotProduct += arr1[i] * arr2[i];
	    }

	    // Calculate the magnitude of each vector
	    double magnitudeArr1 = calculateMagnitude(arr1);
	    double magnitudeArr2 = calculateMagnitude(arr2);

	    // Check if either vector has zero magnitude
	    if (magnitudeArr1 == 0 || magnitudeArr2 == 0) {
	        return 0; // Return 1 if one of the vectors is all zeros
	    }

	    // Calculate and return the cosine similarity
	    //System.out.print(dotProduct / (magnitudeArr1 * magnitudeArr2));
	    return dotProduct / (magnitudeArr1 * magnitudeArr2);
	}

	
	
	
	private static double calculateMagnitude(int[] arr) {
	    double sumOfSquares = 0;
	    for (int value : arr) {
	        sumOfSquares += Math.pow(value, 2);
	    }
	    return Math.sqrt(sumOfSquares);
	}
		
		
	/*
	 * @pre: word is in vocabulary
	 * @pre: the method initModel was called (the language model is initialized), 
	 * @post: $ret = w implies that w is the word with the largest cosineSimilarity(vector for word, vector for w) among all the
	 * other words in vocabulary
	 */
	public String getClosestWord(String word) { // Q - 10

	    //System.out.println("checking:"); // Debugging output

	    int wordIndex2 = getWordIndex("unperceived");
	    
	    //System.out.println(Arrays.toString(mBigramCounts[wordIndex2]));
	    
	    //System.out.println("done check"); // Debugging output

	    
	    int wordIndex = getWordIndex(word);

	    
	    
	    if (wordIndex == ELEMENT_NOT_FOUND) {
	        return null; // Word not in vocabulary
	    }

	    double maxSimilarity = 0;
	    String closestWord = mVocabulary[0]; // Default to the first word

	    int[] wordVector = mBigramCounts[wordIndex];
	    
	    //System.out.println(Arrays.toString(wordVector)); // Debugging output
	    
	    for (int i = 0; i < mVocabulary.length; i++) {
	        if (i != wordIndex) { // Skip comparing with itself
	            int[] currentVector = mBigramCounts[i];
	            double similarity = calcCosineSim(wordVector, currentVector);

	            if (similarity > maxSimilarity || (similarity == maxSimilarity && i < getWordIndex(closestWord))) {
	                maxSimilarity = similarity;
	                closestWord = mVocabulary[i];
	                
	                if (similarity != 0) {
	                    //System.out.println(similarity + " " + "the word " + closestWord);
	                }
	            }
	        }
	    }
	    //System.out.println(closestWord);
	    return closestWord;
	}

	
	
}