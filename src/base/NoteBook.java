package base;

import java.util.ArrayList;

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
}
