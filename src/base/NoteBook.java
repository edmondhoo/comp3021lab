package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook {

	private ArrayList<Folder> folders;

	public NoteBook() {
		folders = new ArrayList<Folder>();
	}

	public ArrayList<Folder> getFolders() {
		return folders;
	}

	public boolean createTextNote(String folderNmae, String title) {
		TextNote note = new TextNote(title);
		return insertNote(folderNmae, note);
	}

	public boolean createImageNote(String folderNmae, String title) {
		ImageNote note = new ImageNote(title);
		return insertNote(folderNmae, note);
	}

	public boolean insertNote(String folderName, Note note) {

		Folder folder = null;

		for (Folder getFolder : folders) {
			if (getFolder.getName().equals(folderName)) {
				folder = getFolder;
				break;
			}
		}
		
		if(folder == null) {
			Folder newFolder = new Folder(folderName);
			newFolder.addNote(note);
			folders.add(newFolder);
		}else {
			
			for(Note getNote : folder.getNotes()) {
				if(getNote.equals(note)) {
					System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
					return false;
				}
			}
			
			folder.addNote(note);
		}
		
		return true;

	}
	
	public void sortFolders() {

		for (Folder getFolder : folders) {
			getFolder.sortNotes();
		}

		Collections.sort(folders);
	}
	
	public boolean createTextNote(String folderName, String title, String content) {
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
	
	public List<Note> searchNotes(String keywords){
		List<Note> searchResult = new ArrayList<Note>();
		for(Folder getFolder: folders) {
			List<Note> getNotes = getFolder.searchNotes(keywords);
			if(getNotes!=null) {
				for(Note getNote : getNotes) {
					searchResult.add(getNote);
					
				}
			}
		}
		return searchResult;
	}
}
