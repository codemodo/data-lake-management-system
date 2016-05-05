package linker.data.entity;

public class Word {

	private int wordID;
	private String wordString;
	
	public Word(int wordID, String wordString) {
		this.wordID = wordID;
		this.wordString = wordString;
	}
	
	public int getID() { return wordID; }
	public String getWordString() { return wordString; }
	
	public String toString() { return getWordString(); }
}
