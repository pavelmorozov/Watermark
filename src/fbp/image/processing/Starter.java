package fbp.image.processing;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Starter extends Application {

	private static final String APP_CAPTION = "Resize & Watermark";
	private final static int IMAGE_SIZE_X = 720;
	private final static int IMAGE_SIZE_Y = 480;
	private final int ADD_Y = 92;
	private static final Integer SCALED_PREVIW_X = 192;
	private static final Integer SCALED_PREVIW_Y = 288;
	private ImageProcessorAWT imageProcessor;
	private Stage primaryStage;
	private Preferences preferences;
	private FileProcessor fileProcessor;
	private Image lastProcessed;
	private double mouseX;
	private double mouseY;

	private TextField sourceField, outputField, watermarkFontSizeField,
			watermarkTextField, watermarkOpacityField, previewFileField,
			rotationTextField, resolutionXField, resolutionYField;
	private GraphicsContext verticalGraphicsContext, horizontalGraphicsContext,
			mainGraphicsContext;
	private Canvas verticalCanvas, horizontalCanvas, mainCanvas;
	private Button chooseSourceBtn, chooseDestinationBtn, choosePreviewFileBtn,
			choosePreviewPrevBtn, choosePreviewNextBtn, processFolderStartBtn,
			processFolderStopBtn, redrawBtn, saveCurrentBtn;
	private Label statusLabel;
	private ColorPicker colorPicker;
	private ComboBox<String> fontComboBox, promoModeComboBox;
	private Scene primaryScene;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws NumberFormatException,
			Exception {
		preferences = Preferences.userRoot().node(this.getClass().getName());
		imageProcessor = new ImageProcessorAWT();
		fileProcessor = new FileProcessor();
		// ///////////////////////////////////////////////////////////////////////////
		// Visual UI controls creation
		// ///////////////////////////////////////////////////////////////////////////

		primaryStage = stage;
		primaryStage.setTitle(APP_CAPTION);

		GridPane rootGrid = new GridPane();
		rootGrid.setId("rootGrid");
		//rootGrid.setGridLinesVisible(true);
		rootGrid.setHgap(10);
		rootGrid.setVgap(10);
		rootGrid.setPadding(new Insets(5, 5, 5, 5));

		Text titleText = new Text(APP_CAPTION);
		titleText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		rootGrid.add(titleText, 0, 0, 5, 1);

		Label sourceLabel = new Label("Source folder:");
		rootGrid.add(sourceLabel, 0, 1);

		sourceField = new TextField();
		sourceField.setId("sourceField");
		rootGrid.add(sourceField, 1, 1, 4, 1);

		chooseSourceBtn = new Button();
		chooseSourceBtn.setText("...");
		rootGrid.add(chooseSourceBtn, 5, 1);

		Label destinationLabel = new Label("Destination folder:");
		rootGrid.add(destinationLabel, 0, 2);

		outputField = new TextField();
		outputField.setId("outputField");
		rootGrid.add(outputField, 1, 2, 4, 1);

		chooseDestinationBtn = new Button();
		chooseDestinationBtn.setText("...");
		rootGrid.add(chooseDestinationBtn, 5, 2);

		Label watermarkTextLabel = new Label("Watermark text:");
		rootGrid.add(watermarkTextLabel, 0, 3);

		watermarkTextField = new TextField();
		rootGrid.add(watermarkTextField, 1, 3, 4, 1);

		redrawBtn = new Button();
		redrawBtn.setText("ReDraw");
		rootGrid.add(redrawBtn, 4, 6);

		Label watermarkFontSizeLabel = new Label("Font size:");
		rootGrid.add(watermarkFontSizeLabel, 0, 4);

		watermarkFontSizeField = new TextField();
		watermarkFontSizeField.setMaxWidth(40);
		rootGrid.add(watermarkFontSizeField, 1, 4, 1, 1);

		// Now create the ComboBox
		fontComboBox = new ComboBox<>();
		fontComboBox.setMaxWidth(155);
		// Fill the ComboBox with all the font names
		fontsToComboBox(fontComboBox);
		// Set the renderer for the combobox
		fontComboBox.setCellFactory(new ComboBoxCellFactory());
		rootGrid.add(fontComboBox, 2, 4, 4, 1);

		Label watermarkOpacityLabel = new Label("Color:");
		rootGrid.add(watermarkOpacityLabel, 0, 5);

		// watermarkOpacityField = new TextField ();
		// watermarkOpacityField.setMaxWidth(40);
		// rootGrid.add(watermarkOpacityField, 1, 5, 1, 1);

		colorPicker = new ColorPicker(Color.web("#ffcce6"));
		rootGrid.add(colorPicker, 1, 5, 2, 1);

		Label rotationLabel = new Label("Rotation:");
		rootGrid.add(rotationLabel, 0, 6);

		rotationTextField = new TextField();
		rotationTextField.setMaxWidth(40);
		rootGrid.add(rotationTextField, 1, 6, 1, 1);

		Label promoLabel = new Label("Promo:");
		rootGrid.add(promoLabel, 0, 7);

		ObservableList<String> modesList = FXCollections
				.observableArrayList("None (Tile)", "Center", "Custom");
		//"Corner: left bottom", "Corner: right bottom",
		//"Corner: left top", "Corner: right top"
		promoModeComboBox = new ComboBox<>(modesList);
		promoModeComboBox.setValue("None (Tile)");
		promoModeComboBox.setMaxWidth(150);
		rootGrid.add(promoModeComboBox, 1, 7, 3, 1);

		Label resolutionLabel = new Label("Resolution:");
		rootGrid.add(resolutionLabel, 0, 8);

		resolutionXField = new TextField();
		resolutionXField.setMaxWidth(40);
		rootGrid.add(resolutionXField, 1, 8, 1, 1);

		resolutionYField = new TextField();
		resolutionYField.setMaxWidth(40);
		rootGrid.add(resolutionYField, 2, 8, 1, 1);

		saveCurrentBtn = new Button();
		// saveCurrentBtn.setMinWidth(100);
		saveCurrentBtn.setText("Save current");
		rootGrid.add(saveCurrentBtn, 3, 8, 2, 1);

		Label previewFileLabel = new Label("Preview:");
		rootGrid.add(previewFileLabel, 0, 9);

		previewFileField = new TextField();
		rootGrid.add(previewFileField, 1, 9, 4, 1);

		choosePreviewFileBtn = new Button();
		choosePreviewFileBtn.setText("...");
		rootGrid.add(choosePreviewFileBtn, 5, 9);

		choosePreviewPrevBtn = new Button();
		choosePreviewPrevBtn.setText("- < -");
		// choosePreviewPrevBtn.setTooltip(new
		// Tooltip("Previous image\n preview"));

		rootGrid.add(choosePreviewPrevBtn, 1, 10);

		choosePreviewNextBtn = new Button();
		choosePreviewNextBtn.setText("- > -");
		// choosePreviewNextBtn.setTooltip(new Tooltip("Next image\n preview"));
		rootGrid.add(choosePreviewNextBtn, 2, 10);

		processFolderStartBtn = new Button();
		processFolderStartBtn.setText("Start");
		rootGrid.add(processFolderStartBtn, 3, 10);

		processFolderStopBtn = new Button();
		processFolderStopBtn.setText("Stop");
		rootGrid.add(processFolderStopBtn, 4, 10);

		statusLabel = new Label("Ready");
		rootGrid.add(statusLabel, 3, 11, 2, 1);

		verticalCanvas = new Canvas(SCALED_PREVIW_X, SCALED_PREVIW_Y);
		verticalGraphicsContext = verticalCanvas.getGraphicsContext2D();
		rootGrid.add(verticalCanvas, 0, 11, 5, 2);

		horizontalCanvas = new Canvas(SCALED_PREVIW_Y, SCALED_PREVIW_X);
		horizontalGraphicsContext = horizontalCanvas.getGraphicsContext2D();
		rootGrid.add(horizontalCanvas, 0, 13, 7, 1);

		mainCanvas = new Canvas(IMAGE_SIZE_X, IMAGE_SIZE_X + ADD_Y);
		mainGraphicsContext = mainCanvas.getGraphicsContext2D();
		rootGrid.add(mainCanvas, 6, 0, 1, 14);

		// ///////////////////////////////////////////////////////////////////////////
		// Initial fields settings
		// ///////////////////////////////////////////////////////////////////////////

		getPreferences();

		fileProcessor.setSourceFolderString(sourceField.getText());
		fileProcessor.listFiles();

		// ///////////////////////////////////////////////////////////////////////////
		// Draw watermark preview
		// ///////////////////////////////////////////////////////////////////////////

		redraw("From file");

		// ///////////////////////////////////////////////////////////////////////////
		// Add listeners
		// ///////////////////////////////////////////////////////////////////////////

		listeners();

		// ///////////////////////////////////////////////////////////////////////////
		// Create primaryScene and show primaryStage
		// ///////////////////////////////////////////////////////////////////////////

		int sceneH = 820;
		int sceneW = 1120;
		int sceneReduce = 0;

		primaryScene = new Scene(rootGrid, sceneW, sceneH - sceneReduce);
		primaryScene.getStylesheets().add(
				Starter.class.getResource("ui.css").toExternalForm());
		primaryStage.setScene(primaryScene);
		primaryStage.show();
		primaryStage.setMaxHeight(primaryStage.getHeight());
		primaryStage.setMinHeight(primaryStage.getHeight() - sceneReduce);
		primaryStage.setMaxWidth(primaryStage.getWidth());
		primaryStage.setMinWidth(primaryStage.getWidth());
	}

	private void chooseFile(String fileChooseMode) {
		if (fileChooseMode.equals("Source")) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.showOpenDialog(primaryStage);
		} else if (fileChooseMode.equals("Output")) {

		}
	}

	/**
	 * Wires UI listeners and actions
	 */

	private void listeners() {
//		watermarkFontSizeField.textProperty().addListener(
//				new ChangeListener<String>() {
//					@Override
//					public void changed(
//							ObservableValue<? extends String> observable,
//							String oldValue, String newValue) {
//						redraw("Current image");
//					}
//				});
		previewFileField.textProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						redraw("From file");
					}
				});

		sourceField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				fileProcessor.setSourceFolderString(sourceField.getText());
				fileProcessor.listFiles();
				SortedSet<File> fileSet = fileProcessor.getFileSet();
				if ((fileSet != null) && (fileSet.size() != 0)) {
					previewFileField.setText(fileSet.first().getPath());
				} else {
					previewFileField.setText("");
				}
			}
		});

		choosePreviewPrevBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SortedSet<File> fileSet = fileProcessor.getFileSet();
				File previousFile = null;
				for (File imageFile : fileSet) {
					if (imageFile.getPath().equals(previewFileField.getText())
							&& (previousFile != null)) {
						previewFileField.setText(previousFile.getPath());
						break;
					}
					previousFile = imageFile;
				}
			}
		});
		choosePreviewNextBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SortedSet<File> fileSet = fileProcessor.getFileSet();
				File currentFile = null;
				for (File imageFile : fileSet) {
					if (currentFile != null) {
						previewFileField.setText(imageFile.getPath());
						break;
					}
					if (imageFile.getPath().equals(previewFileField.getText())) {
						currentFile = imageFile;
					}
				}
			}
		});
		processFolderStartBtn.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				try {
					imageProcessor = new ImageProcessorAWT();
					// this redraw to set up processor
					redraw("Current image");
					final Thread processFolderThread = new Thread(
							imageProcessor);
					statusLabel.textProperty().bind(
							imageProcessor.messageProperty());
					imageProcessor
							.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
								@Override
								public void handle(final WorkerStateEvent arg0) {
									System.out.println("thread succeeded");
									sourceField.setDisable(false);
									outputField.setDisable(false);
									watermarkFontSizeField.setDisable(false);
									watermarkTextField.setDisable(false);
									// watermarkOpacityField.setDisable(false);
									colorPicker.setDisable(false);
									redrawBtn.setDisable(false);
									previewFileField.setDisable(false);
									chooseSourceBtn.setDisable(false);
									chooseDestinationBtn.setDisable(false);
									choosePreviewFileBtn.setDisable(false);
									choosePreviewPrevBtn.setDisable(false);
									choosePreviewNextBtn.setDisable(false);
									processFolderStartBtn.setDisable(false);
									processFolderStopBtn.setDisable(true);
									fontComboBox.setDisable(false);
									promoModeComboBox.setDisable(false);
									rotationTextField.setDisable(false);
									resolutionXField.setDisable(false);
									resolutionYField.setDisable(false);
									saveCurrentBtn.setDisable(false);									
									//Set up processor to current image
									redraw("From file");
								}
							});
					sourceField.setDisable(true);
					outputField.setDisable(true);
					watermarkFontSizeField.setDisable(true);
					watermarkTextField.setDisable(true);
					// watermarkOpacityField.setDisable(true);
					colorPicker.setDisable(true);
					redrawBtn.setDisable(true);
					previewFileField.setDisable(true);
					chooseSourceBtn.setDisable(true);
					chooseDestinationBtn.setDisable(true);
					choosePreviewFileBtn.setDisable(true);
					choosePreviewPrevBtn.setDisable(true);
					choosePreviewNextBtn.setDisable(true);
					processFolderStartBtn.setDisable(true);
					processFolderStopBtn.setDisable(false);
					fontComboBox.setDisable(true);
					promoModeComboBox.setDisable(true);
					rotationTextField.setDisable(true);
					resolutionXField.setDisable(true);
					resolutionYField.setDisable(true);
					saveCurrentBtn.setDisable(true);
					
					processFolderThread.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		processFolderStopBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				imageProcessor.setStopFolderProcess();
			}
		});

		choosePreviewFileBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose file to preview");
				File initialDirectory = new File(sourceField.getText());
				if (initialDirectory.exists() && initialDirectory.isDirectory()) {
					fileChooser.setInitialDirectory(initialDirectory);
				}
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					previewFileField.setText(file.getPath());
					// sourceField.setText(file.getParent());
				}
			}
		});

		chooseSourceBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				directoryChooser.setTitle("Choose source folder");
				File initialDirectory = new File(sourceField.getText());
				if (initialDirectory.exists() && initialDirectory.isDirectory()) {
					directoryChooser.setInitialDirectory(initialDirectory);
				}
				File file = directoryChooser.showDialog(primaryStage);
				if (file != null) {
					sourceField.setText(file.getPath());
					SortedSet<File> fileSet = fileProcessor.getFileSet();
					if ((fileSet != null) && (fileSet.size() != 0)) {
						previewFileField.setText(fileSet.first().getPath());
					} else {
						previewFileField.setText("");
					}
				}

			}
		});

		chooseDestinationBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				directoryChooser.setTitle("Choose output folder");
				File initialDirectory = new File(outputField.getText());
				if (initialDirectory.exists() && initialDirectory.isDirectory()) {
					directoryChooser.setInitialDirectory(initialDirectory);
				}
				File file = directoryChooser.showDialog(primaryStage);
				if (file != null) {
					outputField.setText(file.getPath());
					;
				}
			}
		});

		redrawBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				redraw("Current image");
			}
		});
		
		saveCurrentBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				imageProcessor.setResolutionX(resolutionXField.getText());
				imageProcessor.setResolutionY(resolutionYField.getText());
				redraw("From file");
				try {
					imageProcessor.saveScaledPromoImage();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});	

		promoModeComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				redraw("Current image");

			}
		});

		mainCanvas.setOnMouseEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				if (promoModeComboBox.getValue().equals("Custom")) {
					mainCanvas.setCursor(Cursor.CROSSHAIR);
				} else {
					mainCanvas.setCursor(Cursor.DEFAULT);
				}
			}
		});
		
//		mainCanvas.setOnMouseExited(new EventHandler<Event>() {
//			@Override
//			public void handle(Event arg0) {
//				mainCanvas.setCursor(Cursor.DEFAULT);
//			}
//		});

		mainCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (promoModeComboBox.getValue().equals("Custom")) {
//					String msg = "(x: " + event.getX() + ", y: " + event.getY()
//							+ ") -- " + "(sceneX: " + event.getSceneX()
//							+ ", sceneY: " + event.getSceneY() + ") -- "
//							+ "(screenX: " + event.getScreenX() + ", screenY: "
//							+ event.getScreenY() + ")";
//					System.out.println(msg);
					mouseX = event.getX();
					mouseY = event.getY();
					redraw("Current image");
				}
			}
		});

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				setPreferences();
				imageProcessor.setStopFolderProcess();
			}
		});
	}

	private void getPreferences() {
		try {
			sourceField.setText(preferences.get("sourceField", ""));
			outputField.setText(preferences.get("outputField", ""));
			watermarkFontSizeField.setText(Integer.toString(preferences.getInt(
					"watermarkFontSizeField", 0)));
			// watermarkOpacityField.setText(Double.toString(preferences.getDouble("watermarkOpacityField",
			// 0.0)));
			watermarkTextField.setText(preferences
					.get("watermarkTextField", ""));
			previewFileField.setText(preferences.get("previewFileField", ""));
			colorPicker
					.setValue(Color.valueOf(preferences.get("textColor", "")));
			fontComboBox.setValue(preferences.get("fontString", ""));
			rotationTextField
					.setText(preferences.get("rotationTextField", "0"));
			promoModeComboBox.setValue(preferences.get("promoModeComboBox", ""));
			mouseX = preferences.getDouble("mouseX", 0.0);
			mouseY = preferences.getDouble("mouseY", 0.0);
//			resolutionXField.setText(preferences.get("resolutionXField",""));
//			resolutionYField.setText(preferences.get("resolutionXField",""));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void setPreferences() {
		preferences.put("sourceField", sourceField.getText());
		preferences.put("outputField", outputField.getText());
		preferences.putInt("watermarkFontSizeField",
				Integer.parseInt(watermarkFontSizeField.getText()));
		// preferences.putDouble("watermarkOpacityField",Double.parseDouble(watermarkOpacityField.getText().replace(',',
		// '.')));
		preferences.put("watermarkTextField", watermarkTextField.getText());
		preferences.put("previewFileField", previewFileField.getText());
		preferences.put("textColor", colorPicker.getValue().toString());
		preferences.put("fontString", fontComboBox.getValue());
		preferences.put("rotationTextField", rotationTextField.getText());
		preferences.put("promoModeComboBox",promoModeComboBox.getValue());
		preferences.putDouble("mouseX", mouseX);
		preferences.putDouble("mouseY", mouseY);
//		preferences.put("resolutionXField", resolutionXField.getText());
//		preferences.put("resolutionYField", resolutionYField.getText());
	}

	/**
	 * First set up image processor, procees, and redraw preview images on
	 * screen.
	 * 
	 * @param redrawMode
	 *            points to use current image or get it from file
	 */
	private void redraw(String redrawMode) {
		imageProcessor.setWatermarkText(watermarkTextField.getText());
		imageProcessor.setFontSize(Integer.parseInt(watermarkFontSizeField
				.getText()));
		// imageProcessor.setOpacity(Double.parseDouble(watermarkOpacityField.getText().replace(',',
		// '.')));
		imageProcessor.setTextColor(colorPicker.getValue());
		imageProcessor.setSourceFolder(sourceField.getText());
		imageProcessor.setOutputFolder(outputField.getText());
		imageProcessor.setFontString(fontComboBox.getValue());
		imageProcessor
				.setRotation(Integer.parseInt(rotationTextField.getText()));
		imageProcessor.setPromoMode(promoModeComboBox.getValue());
		imageProcessor.setX(mouseX);
		imageProcessor.setY(mouseY);
		imageProcessor.setGraphicsContext(mainGraphicsContext);

		//imageProcessor.setGraphicsContext(verticalGraphicsContext);
		verticalGraphicsContext.setFill(Color.web("#ffffff", 1));
		verticalGraphicsContext
				.fillRect(0, 0, SCALED_PREVIW_X, SCALED_PREVIW_Y);
		Image previewImage = verticalCanvas.snapshot(null, null);
		imageProcessor.setImageToProcess(previewImage);
		//imageProcessor.setGraphicsContext(verticalGraphicsContext);
		imageProcessor.processImage();
		previewImage = imageProcessor.getImageProcessed();
		verticalGraphicsContext.drawImage(previewImage, 0, 0,
				verticalCanvas.getWidth(), verticalCanvas.getHeight());

		horizontalGraphicsContext.setFill(Color.web("#ffffff", 1));
		horizontalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_Y,
				SCALED_PREVIW_X);
		previewImage = horizontalCanvas.snapshot(null, null);
		imageProcessor.setImageToProcess(previewImage);
		//imageProcessor.setGraphicsContext(horizontalGraphicsContext);
		imageProcessor.processImage();
		previewImage = imageProcessor.getImageProcessed();
		horizontalGraphicsContext.drawImage(previewImage, 0, 0,
				horizontalCanvas.getWidth(), horizontalCanvas.getHeight());
		
		if (redrawMode.equals("Current image")) {
			//Image already set, so nothing to do
			imageProcessor.setImageToProcess(lastProcessed);
		} else if (redrawMode.equals("From file")) {
			imageProcessor
					.setImageToProcessFromFile(previewFileField.getText());
			lastProcessed = imageProcessor.getImageToProcess();
		}		
		
		resolutionXField.setText(Double.toString(
				imageProcessor.getImageToProcess().getWidth()));
		resolutionYField.setText(Double.toString(
				imageProcessor.getImageToProcess().getHeight()));		
		
		//imageProcessor.setGraphicsContext(mainGraphicsContext);
		imageProcessor.processImage();
		imageProcessor.fillMainGraphicsContext();
		
//		mainGraphicsContext.setFill(Color.web("#555555", 1));
//		mainGraphicsContext.fillRect(0, 0, IMAGE_SIZE_H, IMAGE_SIZE_X + ADD_Y);
//
//		if (redrawMode.equals("Current image")) {
//			// Image already set, so nothing to do
//			imageProcessor.setImageToProcess(lastProcessed);
//		} else if (redrawMode.equals("From file")) {
//			imageProcessor
//					.setImageToProcessFromFile(previewFileField.getText());
//			lastProcessed = imageProcessor.getImageToProcess();
//		}
//
//		imageProcessor.processImage();
//		previewImage = imageProcessor.getImageProcessed();
//
//		double x = 0;
//		double y = 0;
//
//		if (previewImage.getWidth() < previewImage.getHeight()) {
//			// vertical align
//			x = (mainCanvas.getWidth() - previewImage.getWidth()) / 2;
//			y = (mainCanvas.getHeight() - previewImage.getHeight()) / 2;
//			// y = addY/2;//y = 0;
//		} else {
//			// horizontal align
//			x = 0;
//			y = (mainCanvas.getHeight() - previewImage.getHeight()) / 2;
//		}
//
//		mainGraphicsContext.drawImage(previewImage, x, y,
//				previewImage.getWidth(), previewImage.getHeight());
	}

	/**
	 * Fill the given combobox with Fonts
	 * 
	 * @param fontComboBox
	 *            comboBox to fill with font names
	 */
	private void fontsToComboBox(ComboBox<String> fontComboBox) {
		List<String> fontNames = Font.getFontNames();
		ObservableList<String> items = fontComboBox.getItems();
		for (String fontName : fontNames) {
			items.add(fontName);
		}
	}
}
