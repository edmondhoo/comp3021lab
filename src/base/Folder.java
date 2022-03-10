package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder> {
	private ArrayList<Note> notes;
	private String name;

	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public String getName() {
		return name;
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}

	@Override
	public String toString() {
		int nText = 0;
		int nImage = 0;

		for (Note note : notes) {

			if (note instanceof ImageNote)
				nImage++;

			if (note instanceof TextNote)
				nText++;
		}

		return name + ":" + nText + ":" + nImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Folder o) {
		// TODO Auto-generated method stub
		if (this.name.compareTo(o.name) < 0) {
			return -1;
		} else if (this.name.compareTo(o.name) > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	public void sortNotes() {
		Collections.sort(notes);
	}

	public List<Note> searchNotes(String keywords) {
		List<Note> searchResult = new ArrayList<Note>();

		String convertKeywords[] = keywords.toUpperCase().split(" ");
		for (Note getNote : notes) {

			boolean resultOr = true;
			boolean result = true;
			
			if (getNote instanceof ImageNote) {
				ImageNote imageNote = (ImageNote) getNote;
				
				for (int i = 0; i < convertKeywords.length; i++) {

					if (convertKeywords[i].equals("OR")) {
						if(imageNote.getTitle().toUpperCase().contains(convertKeywords[i - 1])|| imageNote.getTitle().toUpperCase().contains(convertKeywords[i + 1])) {
							resultOr = true;
							
							if((i+1)<convertKeywords.length) {
								i += 1;
							}
						}else {
							resultOr = false;
							i+=1;
						}
					} else {
						if((i + 1) < convertKeywords.length) {
							if(!convertKeywords[i+1].equals("OR")) {
								if(imageNote.getTitle().toUpperCase().contains(convertKeywords[i])) {
									result = true;
								}else {
									result = false;
									break;
								}
							}else {
								continue;
							}
							
							
						}else {
							if(imageNote.getTitle().toUpperCase().contains(convertKeywords[i])) {
								result = true;
							}else {
								result = false;
								break;
							}
						}
					}			
				}
			} 
			
			else if (getNote instanceof TextNote) {
				TextNote imageNote = (TextNote) getNote;
				
				for (int i = 0; i < convertKeywords.length; i++) {

					if (convertKeywords[i].equals("OR")) {
						if(imageNote.getTitle().toUpperCase().contains(convertKeywords[i - 1])
								|| imageNote.getTitle().toUpperCase().contains(convertKeywords[i + 1])
								||imageNote.getContent().toUpperCase().contains(convertKeywords[i - 1])
								|| imageNote.getContent().toUpperCase().contains(convertKeywords[i + 1])) {
							resultOr = true;
							
							if((i+1)<convertKeywords.length) {
								i += 1;
							}
						}else {
							resultOr = false;
							i+=1;
						}
					} else {
						if((i + 1) < convertKeywords.length) {
							if(!convertKeywords[i+1].equals("OR")) {
								if(imageNote.getTitle().toUpperCase().contains(convertKeywords[i]) || imageNote.getContent().toUpperCase().contains(convertKeywords[i])) {
									result = true;
								}else {
									result = false;
									break;
								}
							}else {
								continue;
							}
							
							
						}else {
							if(imageNote.getTitle().toUpperCase().contains(convertKeywords[i]) || imageNote.getContent().toUpperCase().contains(convertKeywords[i])) {
								result = true;
							}else {
								result = false;
								break;
							}
						}
					}			
				}		
			}
			
			if (result && resultOr)
				searchResult.add(getNote);		
		}

		return searchResult;
	}
}
