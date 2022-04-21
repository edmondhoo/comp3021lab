package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	
	Stage stage;
	
	String currentNote = "";

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		this.stage = stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
		
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		//Load button
		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);	
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a Note Object!");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)","*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(stage);
				
				if(file != null) {
					loadNoteBook(file);
					updateListView();
				}
			}			
		});
		
		
		//Save button
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save");
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)","*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				fileChooser.setInitialFileName("new_test.ser");
				File file = fileChooser.showSaveDialog(stage);			
				if(file!=null) {
					if(save(noteBook,file)) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Successfully saved");
						alert.setContentText("You file has been saved to file " + file.getName());
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
					}
				}
			}
		});
		
		
		
		Label search = new Label("Search : ");
		TextField textSearch = new TextField("");
		
		Button searchButton = new Button("Search");
		searchButton.setPrefSize(100, 20);
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentSearch = textSearch.getText();
				textAreaNote.setText("");				
				updateListView(true);		
			}
			
		});
		
		
		Button clearSearchButton = new Button("Clear Search");
		clearSearchButton.setPrefSize(100, 20);
		clearSearchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				textSearch.setText("");
				updateListView(false);
			}
			
		});

		
		hbox.getChildren().addAll(buttonLoad, buttonSave,search,textSearch,searchButton,clearSearchButton);
		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		for(Folder getFolder: noteBook.getFolders()) {
			foldersComboBox.getItems().add(getFolder.getName());
		}
	
		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if(t1!=null)
					currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				updateListView(false);

			}

		});
		foldersComboBox.setValue("-----");
		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				String content = "";
				for(Folder getFolder: noteBook.getFolders()) {
					if(getFolder.getName().equals(currentFolder)) {
						for(Note getNote : getFolder.getNotes()) {
							if(getNote.getTitle().equals(title)) {
								content = ((TextNote) getNote).getContent();
							}
						}
					}
				}
				
				textAreaNote.setText(content);

			}
		});
		
		HBox hbox = new HBox();
		hbox.setSpacing(10);
		
		//Button: Add a Folder
		Button addFolder = new Button("Add a Folder");
		addFolder.setPrefSize(100, 20);	
		addFolder.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
			    dialog.setTitle("Input");
			    dialog.setHeaderText("Add a new folder for your notebook:");
			    dialog.setContentText("Please enter the name you want to create:");
			    
			    Optional<String> result = dialog.showAndWait();
			    if (result.isPresent()){
					if (result.get().isEmpty()) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setContentText("Please input an valid folder name");
						alert.showAndWait().ifPresent(rs -> {
							if (rs == ButtonType.OK) {
								System.out.println("Pressed OK.");
							}
						});
					}else {
						boolean addNewFolder = true;
						for (Folder getFolder : noteBook.getFolders()) {
							if (getFolder.getName().equals(result.get())) {
								addNewFolder = false;
								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("Warning");
								alert.setContentText("You already have a folder named with " + result.get());
								alert.showAndWait().ifPresent(rs -> {
									if (rs == ButtonType.OK) {
										System.out.println("Pressed OK.");
									}
								});
							}
						}
						
						if(addNewFolder) {
							addFolder(result.get());
						}
					}
			    }


			}			
		});
		
		//Button: Add a Note
		Button addNote = new Button("Add a Note");
		addNote.setPrefSize(100, 20);	
		addNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(foldersComboBox.getValue() == null || foldersComboBox.getValue().equals("-----")) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please choose a folder first!");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}else {
					TextInputDialog dialog = new TextInputDialog("Add a Note");
				    dialog.setTitle("Input");
				    dialog.setHeaderText("Add a new note to current folder");
				    dialog.setContentText("Please enter the name of your note:");
				    
				    Optional<String> result = dialog.showAndWait();
				    if (result.isPresent()){
				    	String name = foldersComboBox.getValue();
				    	if(noteBook.createTextNote(name, result.get())) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Successful!");
							alert.setContentText("Insert note "+ result.get() + " to folder " + name+" successfully!");
							alert.showAndWait().ifPresent(rs -> {
							    if (rs == ButtonType.OK) {
							        System.out.println("Pressed OK.");
							    }
							});
							updateListView();
							foldersComboBox.setValue(name);
				    	}
				    }
				}
			}			
		});
		
		hbox.getChildren().addAll(foldersComboBox,addFolder);
		
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addNote);

		return vbox;
	}

	private void updateListView(boolean search) {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		for (Folder getFolder : noteBook.getFolders()) {
			if (getFolder.getName().equals(currentFolder)) {
				if (search == false) {
					for (Note getNote : getFolder.getNotes()) {
						list.add(getNote.getTitle());
					}
				}else {
					for(Note getNote : getFolder.searchNotes(currentSearch)) {
						list.add(getNote.getTitle());
					}
				}
			}
		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		
		//Button: Save Note
		Button saveNote = new Button("Save Note");
		saveNote.setPrefSize(100, 20);	
		saveNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(foldersComboBox.getValue() == null || foldersComboBox.getValue().equals("-----") || titleslistView.getSelectionModel().getSelectedItem() == null) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}else {
					String name = null;
					String text = textAreaNote.getText();
					for(Folder getFolder : noteBook.getFolders()) {
						if(foldersComboBox.getValue().equals(getFolder.getName())) {
							name = getFolder.getName();
							for(Note getNote : getFolder.getNotes()) {
								if(titleslistView.getSelectionModel().getSelectedItem().equals(getNote.getTitle())) {
									((TextNote) getNote).setContent(textAreaNote.getText());
									break;
								}
							}
							break;
						}
					}
					updateListView();
					foldersComboBox.setValue("");
				}
			}			
		});
		
		//Button: Delete Note
		Button deleteNote = new Button("Delete Note");
		deleteNote.setPrefSize(100, 20);	
		deleteNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(foldersComboBox.getValue() == null || foldersComboBox.getValue().equals("-----") || titleslistView.getSelectionModel().getSelectedItem() == null) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait().ifPresent(rs -> {
						if (rs == ButtonType.OK) {
							System.out.println("Pressed OK.");
						}
					});
				}else {
					String name = foldersComboBox.getValue();
					if(removeNotes(titleslistView.getSelectionModel().getSelectedItem())) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Succeed!");
						alert.setHeaderText("Confirmation");
						alert.setContentText("Your note has been successfully removed");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
						
						foldersComboBox.setValue("");
						foldersComboBox.setValue(name);
					}
				}
			}			
		});
		
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);
		
		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitHeight(18);
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);
		
		hBox.getChildren().addAll(saveView,saveNote,deleteView,deleteNote);

		
		grid.add(hBox, 0, 0);
		grid.add(textAreaNote, 0, 1);

		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called â€œthe most shocking play in NFL historyâ€� and the Washington Redskins dubbed the â€œThrowback Specialâ€�: the November 1985 play in which the Redskinsâ€™ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Awardâ€“winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everythingâ€”until it wasnâ€™t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliantâ€”a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwetherâ€™s Daddy Was a Number Runner and Dorothy Allisonâ€™s Bastard Out of Carolina, Jacqueline Woodsonâ€™s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthoodâ€”the promise and peril of growing upâ€”and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}
	
	private void loadNoteBook(File file) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			NoteBook fileObject = (NoteBook) in.readObject();
//			NoteBook nb = new NoteBook();
//			for(Folder getFolder : fileObject.getFolders()) {
//				for(Note getNote : getFolder.getNotes()) {
//					if(getNote instanceof TextNote) {
//						nb.createTextNote(getFolder.getName(), getNote.getTitle(), ((TextNote) getNote).getContent());
//					}
//					
//					if(getNote instanceof ImageNote) {
//						nb.createImageNote(getFolder.getName(), getNote.getTitle());
//					}
//				}
//			}
//			in.close();
//			
//			noteBook = nb;
			noteBook = fileObject;
			in.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		foldersComboBox.getItems().clear();
		for (Folder getFolder : noteBook.getFolders()) {
			foldersComboBox.getItems().add(getFolder.getName());
			for (Note getNote : getFolder.getNotes()) {
				list.add(getNote.getTitle());
			}

		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
		updateListView(false);
	}
	
	public boolean save(NoteBook notebook, File file) {
	
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			out.writeObject(notebook);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	public void addFolder(String folderName) {
		NoteBook newNoteBook = new NoteBook();
		for (Folder getFolder : noteBook.getFolders()) {
			newNoteBook.addFolder(getFolder);
		}
		Folder newFolder = new Folder(folderName);
		newNoteBook.addFolder(newFolder);
		noteBook = newNoteBook;
		updateListView();
		foldersComboBox.setValue(folderName);
	}

	public boolean removeNotes(String title) {
		   // TODO
		   // Given the title of the note, delete it from the folder.
		   // Return true if it is deleted successfully, otherwise return false. 
		for(Folder getFolder : noteBook.getFolders()) {
			for(Note getNode : getFolder.getNotes()) {
				if(getNode.getTitle().equals(title)) {
					currentFolder =  getFolder.getName();
					getFolder.getNotes().remove(getNode);
					return true;
				}
				
			}
		}
		return false;
	}


}
