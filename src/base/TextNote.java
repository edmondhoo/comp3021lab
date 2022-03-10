package base;

public class TextNote extends Note{
	
	String content;
	
	TextNote(String title){
		super(title);
	}
	
	TextNote(String title, String content) {
		super(title);
		this.content = content;
	}
	
	public String getContent() {
		return this.content;
	}
}
