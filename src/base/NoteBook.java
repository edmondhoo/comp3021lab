package base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook implements java.io.Serializable{

	private ArrayList<Folder> folders;

	public NoteBook() {
		folders = new ArrayList<Folder>();
	}
	
	public NoteBook(String file) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook n = (NoteBook) in.readObject();
			folders = n.folders;
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Folder> getFolders() {
		return folders;
	}
	
	public void addFolder(Folder folder) {
		folders.add(folder);
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
	
	public boolean save(String file) {
		NoteBook object = new NoteBook();
		object.folders = this.folders;
	
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(object);
			out.close();		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
