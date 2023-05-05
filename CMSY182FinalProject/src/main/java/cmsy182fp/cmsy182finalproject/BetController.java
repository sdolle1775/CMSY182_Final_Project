/**
 * BetController- javaFx controller class for Bet Recorder
 * CMSY167 Spring 2023
 * @author Samuel Dolle
 * @version 1.0
 *
 */
package cmsy182fp.cmsy182finalproject;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.lang.Math;
import java.time.LocalDate;
import java.util.Scanner;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.scene.media.AudioClip; //edited module-info.java and pom.xml; also added Maven dependency


public class BetController {
    //declare fxml variables
    @FXML
    private DatePicker DateEntry;
    @FXML
    private TextField RecordName;
    @FXML
    private Label MessageLabel;
    @FXML
    private TextField WagerEntry;
    @FXML
    private Slider WagerSlider;
    @FXML
    private TextField TotalOdds;
    @FXML
    private TextField ExpectedPayout;
    @FXML
    private TextField ExpectedWinnings;
    @FXML
    private TextField TotalWinnings;
    @FXML
    private Button CalculateParlay;
    @FXML
    private Button CalculatePayout;
    @FXML
    private TextField Leg1;
    @FXML
    private TextField Leg2;
    @FXML
    private TextField Leg3;
    @FXML
    private TextField Leg4;
    @FXML
    private TextField Leg5;
    @FXML
    private TextField Leg6;
    @FXML
    private TextField Leg7;
    @FXML
    private TextField Leg8;
    @FXML
    private TextField Leg9;
    @FXML
    private TableView<BetRecord> TableView;
    @FXML
    private TableColumn<BetRecord, LocalDate> DateColumn;
    @FXML
    private TableColumn<BetRecord, String> NameColumn;
    @FXML
    private TableColumn<BetRecord, Double> AmountWageredColumn;
    @FXML
    private TableColumn<BetRecord, Double> OddsColumn;
    @FXML
    private TableColumn<BetRecord, Double> AmountWonColumn;

    private String initialMessage;
    private Double initialSliderValue;
    private Integer initialLegValue;

    String fileName = "src/main/java/cmsy182fp/cmsy182finalproject/betrecorderoutput.txt"; //file path URL for text file where bets are recorded
    File file = new File(fileName);
    ObservableList<BetRecord> record = FXCollections.observableArrayList(); //create observable list of BetRecord objects and add to FX Collection in observable array list

    public void initList(){
        try {
            Scanner input = new Scanner(file); //load text file into scanner
            while ((input.hasNext())){
                String data = input.nextLine();
                String[] values_line = data.split("!@#%"); //split strings by delimiter
                //scanning each line, add strings as object parameters for Bet Record then add each record to observableArrayList
                record.add(new BetRecord(LocalDate.parse(values_line[0]),(values_line[1]),Double.valueOf(values_line[2]),Double.valueOf(values_line[3]),Double.valueOf(values_line[4])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        initList(); //load text file first
        initialMessage = MessageLabel.getText();
        initialSliderValue = WagerSlider.getValue();
        ExpectedWinnings.setEditable(false); //set these text fields as non editable
        ExpectedPayout.setEditable(false);
        TotalWinnings.setEditable(false);
        //set default values
        initialLegValue = 1;
        RecordName.setText("Untitled");
        DateEntry.setValue(LocalDate.now());
        //name and instantiate TableView columns
        DateColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, LocalDate>("date"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, String>("name"));
        AmountWageredColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, Double>("amountWagered"));
        OddsColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, Double>("odds"));
        AmountWonColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, Double>("amountWon"));
        //allow for editing of cells in NameColumn
        TableView.setEditable(true);
        NameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableView.setItems(record); //populate table
        showTotalWinnings(); //update Total Winnings field

    }

    public void playChachingSound() { //play sound using external maven dependency
        AudioClip chaching= new AudioClip(this.getClass().getResource("chaching.wav").toString()); //create audioclip file
        chaching.play();
    }

    public void playButtonSound() {
        AudioClip buttonsound= new AudioClip(this.getClass().getResource("button.wav").toString());
        buttonsound.play();
    }

    public void showTotalWinnings() { //display Total Winnings field
        Double total = 0.0;
        Double wagers = 0.0;
        Double winnings = 0.0;
        for (int i= 0;i<TableView.getItems().size();i++){
            //retrieve sum value of all cells in columns 2 (AmountWagered) and 4 (AmountWon) stored separately
            wagers = wagers+Double.valueOf(String.valueOf(TableView.getColumns().get(2).getCellObservableValue(i).getValue()));
            winnings = winnings+Double.valueOf(String.valueOf(TableView.getColumns().get(4).getCellObservableValue(i).getValue()));
        }
        total = winnings - wagers; //subtract wagers from winnings to find total winnings
        if (total >0) {
            TotalWinnings.setStyle("-fx-text-fill: green"); //css text fill change dependent on positive or negative total winnings
        } else {
            TotalWinnings.setStyle("-fx-text-fill: red");
        }
        TotalWinnings.setText(String.format("%,.2f", total));

    }

    public void changeNameCellEvent(CellEditEvent editedCell) { //allow for Names of records to be edited
        BetRecord recordSelected = TableView.getSelectionModel().getSelectedItem();
        recordSelected.setName(editedCell.getNewValue().toString()); //allow for editing name directly in cell
    }

    @FXML
    public void SaveFilePressed(ActionEvent event) { //overwrite "betrecorderoutput.txt" file to save progress
        playButtonSound(); //call sound effect method
        try { //use bufferedwriter and stringbuilder to format and write text
            BufferedWriter outwriter = new BufferedWriter(new FileWriter(file));
            StringBuilder outstr = new StringBuilder("");

            for(BetRecord entries: record) { //create string using stringbuilder for each record
                //string builder format grabs all fields of BetRecord object seperated by throwaway characters
                outstr.append(entries.getDate().toString() + "!@#%" + entries.getName().toString() + "!@#%" + entries.getAmountWagered().toString() + "!@#%" + entries.getOdds().toString() + "!@#%" + entries.getAmountWon().toString());
                String output = outstr.toString();
                outwriter.write(output);
                outwriter.newLine(); //add newlines between entries
                outstr.setLength(0); //clear string builder
            }

            MessageLabel.setTextFill(Color.web("#00FF00")); //change message color to green
            MessageLabel.setText("File has been successfully saved."); //success confirmation
            outwriter.close();

        } catch (IOException e) {
            MessageLabel.setTextFill(Color.web("#a30000")); //change message color to red
            MessageLabel.setText("Parsing Error: Check your inputs."); //error message
        }
    }

    @FXML
    void RemoveEntryPressed(ActionEvent event) { //delete selected record in ViewTable
        playButtonSound();
        ObservableList<BetRecord> selectedRows, allRecords;
        allRecords = TableView.getItems();

        selectedRows = TableView.getSelectionModel().getSelectedItems(); //retrieve all items in selected row

        for (BetRecord record: selectedRows) {
            allRecords.remove(record); //remove all entries for row selected
        }
        MessageLabel.setTextFill(Color.web("#000000")); //change message color to black
        MessageLabel.setText("Entry Removed");
        showTotalWinnings(); //update total winnings field
    }

    @FXML
    void ResetButtonPressed(ActionEvent event) { //clear enter-able text fields
        playButtonSound();
        MessageLabel.setTextFill(Color.web("#000000"));
        MessageLabel.setText(initialMessage);
        RecordName.setText("Untitled");
        WagerEntry.clear();
        WagerSlider.setValue(initialSliderValue);
        TotalOdds.clear();
        ExpectedPayout.clear();
        ExpectedWinnings.clear();
        Leg1.clear();
        Leg2.clear();
        Leg3.clear();
        Leg4.clear();
        Leg5.clear();
        Leg6.clear();
        Leg7.clear();
        Leg8.clear();
        Leg9.clear();
    }

    @FXML
    void WonButtonPressed(ActionEvent event) { //submit bet as won record
        playChachingSound(); //play chaching sound effect
        try { //store current values of appropriate fields, parsing as double where necessary
            String entry = WagerEntry.getText();
            Double entrydouble = Double.parseDouble(entry);
            String odds = TotalOdds.getText();
            Double oddsdouble = Double.parseDouble(odds);
            String winnings = ExpectedWinnings.getText();
            Double winningsdouble = Double.parseDouble(winnings);
            //enter variable as new object parameters
            BetRecord newRecord = new BetRecord(DateEntry.getValue(), RecordName.getText(), entrydouble, oddsdouble, winningsdouble);
            //enter new object in table
            TableView.getItems().add(newRecord);
            showTotalWinnings(); // update total winnings
            MessageLabel.setTextFill(Color.web("#00FF00"));
            MessageLabel.setText("Bet successfully recorded as won.");
        } catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");
        }
    }

    @FXML
    void LostButtonPressed(ActionEvent event) { //submit bet as lost record
        playButtonSound();
        try {
            String entry = WagerEntry.getText();
            Double entrydouble = Double.parseDouble(entry);
            String odds = TotalOdds.getText();
            Double oddsdouble = Double.parseDouble(odds);
            //enter variables as new object parameters, entering 0.00 for amount won
            BetRecord newRecord = new BetRecord(DateEntry.getValue(), RecordName.getText(), entrydouble, oddsdouble, 0.00);

            TableView.getItems().add(newRecord);
            showTotalWinnings();
            MessageLabel.setTextFill(Color.web("#000000"));
            MessageLabel.setText("Bet successfully recorded as lost.");
        } catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");
        }
    }

    @FXML
    void PayoutButtonPressed(ActionEvent event) { //calculate payout based on wager and odds
        playButtonSound();
        try { //parse all relevant fields as doubles
            double WagerValue = Double.parseDouble(WagerEntry.getText());
            double OddsValue = Double.parseDouble(TotalOdds.getText());
            double SliderValue = WagerSlider.getValue(); // slider used for quick multiplication of wager by either x.5,x1,x1.5,or x2.0
            double OddsDecimal; //odds must first be converted from american to decimal odds
            if (OddsValue<0) { //if american odds are negative the absolute value is taken then 100 divided by the result then +1
                double OddsNegative = Math.abs(OddsValue);
               OddsDecimal = 100/OddsNegative +1;
            } else { //if american odds are positive they are divided by 100 then +1
                OddsDecimal = OddsValue/100 +1;
            }
            double PayoutValue = (WagerValue*SliderValue)*OddsDecimal; //multiply wager by slider value (default 1) then by new decimal odds
            double WinningsValue = PayoutValue-(WagerValue*SliderValue); // winnings equal payout minus wager value
            WagerEntry.setText(String.valueOf(WagerValue*SliderValue));
            ExpectedPayout.setText(String.format("%.2f", PayoutValue)); //update appropriate fields
            ExpectedWinnings.setText(String.format("%.2f", WinningsValue));
            WagerSlider.setValue(initialSliderValue); //reset slider value to 1
            MessageLabel.setTextFill(Color.web("#000000"));
            MessageLabel.setText("Payout successfully calculated.");

        }catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");
        }
    }

    @FXML
    void ParlayButtonPressed(ActionEvent event) { //calculate parlays which are bets involving multiple legs, or combined odds
        playButtonSound();
        try {
            double LegOneDecimal; //again american odds must first be converted to decimal odds by the same process
            if (Leg1.getText().isBlank()) { //if leg field is blank value is set to 1
                LegOneDecimal=initialLegValue;
            } else {
                double LegOne = Double.parseDouble(Leg1.getText());
                if (LegOne < 0) {
                    double LegOneNegative = Math.abs(LegOne);
                    LegOneDecimal = 100 / LegOneNegative + 1;
                } else {
                    LegOneDecimal = LegOne / 100 + 1;
                }
            }

            double LegTwoDecimal;
            if (Leg2.getText().isBlank()) {
                LegTwoDecimal=initialLegValue;
            } else {
                double LegTwo = Double.parseDouble(Leg2.getText());
                if (LegTwo < 0) {
                    double LegTwoNegative = Math.abs(LegTwo);
                    LegTwoDecimal = 100 / LegTwoNegative + 1;
                } else {
                    LegTwoDecimal = LegTwo / 100 + 1;
                }
            }

            double LegThreeDecimal;
            if (Leg3.getText().isBlank()) {
                LegThreeDecimal=initialLegValue;
            } else {
                double LegThree = Double.parseDouble(Leg3.getText());
                if (LegThree < 0) {
                    double LegThreeNegative = Math.abs(LegThree);
                    LegThreeDecimal = 100 / LegThreeNegative + 1;
                } else {
                    LegThreeDecimal = LegThree / 100 + 1;
                }
            }

            double LegFourDecimal;
            if (Leg4.getText().isBlank()) {
                LegFourDecimal=initialLegValue;
            } else {
                double LegFour = Double.parseDouble(Leg4.getText());
                if (LegFour < 0) {
                    double LegFourNegative = Math.abs(LegFour);
                    LegFourDecimal = 100 / LegFourNegative + 1;
                } else {
                    LegFourDecimal = LegFour / 100 + 1;
                }
            }
            
            double LegFiveDecimal;
            if (Leg5.getText().isBlank()) {
                LegFiveDecimal=initialLegValue;
            } else {
                double LegFive = Double.parseDouble(Leg5.getText());
                if (LegFive < 0) {
                    double LegFiveNegative = Math.abs(LegFive);
                    LegFiveDecimal = 100 / LegFiveNegative + 1;
                } else {
                    LegFiveDecimal = LegFive / 100 + 1;
                }
            }

            double LegSixDecimal;
            if (Leg6.getText().isBlank()) {
                LegSixDecimal=initialLegValue;
            } else {
                double LegSix = Double.parseDouble(Leg6.getText());
                if (LegSix < 0) {
                    double LegSixNegative = Math.abs(LegSix);
                    LegSixDecimal = 100 / LegSixNegative + 1;
                } else {
                    LegSixDecimal = LegSix / 100 + 1;
                }
            }

            double LegSevenDecimal;
            if (Leg7.getText().isBlank()) {
                LegSevenDecimal=initialLegValue;
            } else {
                double LegSeven = Double.parseDouble(Leg7.getText());
                if (LegSeven < 0) {
                    double LegSevenNegative = Math.abs(LegSeven);
                    LegSevenDecimal = 100 / LegSevenNegative + 1;
                } else {
                    LegSevenDecimal = LegSeven / 100 + 1;
                }
            }

            double LegEightDecimal;
            if (Leg8.getText().isBlank()) {
                LegEightDecimal=initialLegValue;
            } else {
                double LegEight = Double.parseDouble(Leg8.getText());
                if (LegEight < 0) {
                    double LegEightNegative = Math.abs(LegEight);
                    LegEightDecimal = 100 / LegEightNegative + 1;
                } else {
                    LegEightDecimal = LegEight / 100 + 1;
                }
            }

            double LegNineDecimal;
            if (Leg9.getText().isBlank()) {
                LegNineDecimal=initialLegValue;
            } else {
                double LegNine = Double.parseDouble(Leg9.getText());
                if (LegNine < 0) {
                    double LegNineNegative = Math.abs(LegNine);
                    LegNineDecimal = 100 / LegNineNegative + 1;
                } else {
                    LegNineDecimal = LegNine / 100 + 1;
                }
            }
            //decimal odds of all legs are multiplied together, blank leg field values are set to 1 and so do not affect outcome
            double ParlayDecimal = LegOneDecimal*LegTwoDecimal*LegThreeDecimal*LegFourDecimal*LegFiveDecimal*LegSixDecimal*LegSevenDecimal*LegEightDecimal*LegNineDecimal;
            double ParlayOdds;
            if (ParlayDecimal<2.0) { //if combined decimal odds is < 2, is equivalent to negative american odds, -100 is divided by (result minus 1)
               ParlayOdds = -100/(ParlayDecimal-1);
            }else{ //if combined decimal odds is >= 2, is equivalent to positive american odds, (result minus 1) is multiplied by 100
                ParlayOdds = (ParlayDecimal-1)*100;
            } //if all leg field values equal 1, no data was entered in appropriate fields, throw error message
            if (LegOneDecimal==1&&LegTwoDecimal==1&&LegThreeDecimal==1&&LegFourDecimal==1&&LegFiveDecimal==1&&LegSixDecimal==1&&LegSevenDecimal==1&&LegEightDecimal==1&&LegNineDecimal==1) {
                MessageLabel.setTextFill(Color.web("#a30000"));
                MessageLabel.setText("Error: No inputs in leg fields.");
            } else {
                double ParlayRounded = Math.round(ParlayOdds); //round parlay odds as is done with sportsbooks
                TotalOdds.setText(String.valueOf(ParlayRounded));
                MessageLabel.setTextFill(Color.web("#000000"));
                MessageLabel.setText("Parlay successfully calculated.");
            }
        }catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");
        }
    }
}
