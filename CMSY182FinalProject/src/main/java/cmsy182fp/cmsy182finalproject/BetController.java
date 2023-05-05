package cmsy182fp.cmsy182finalproject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.lang.Math;
import java.time.LocalDate;
import java.time.Month;
import java.util.Scanner;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;

public class BetController {
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

    //list of Highscore items

    String fileName = "src/main/java/cmsy182fp/cmsy182finalproject/betrecorderoutput.txt";
    File file = new File(fileName);
    ObservableList<BetRecord> record = FXCollections.observableArrayList();

    public void initList(){
        try {
            Scanner input = new Scanner(file);
            while ((input.hasNext())){
                String data = input.nextLine();
                String[] values_line = data.split("##");

                record.add(new BetRecord(LocalDate.parse(values_line[0]),(values_line[1]),Double.valueOf(values_line[2]),Double.valueOf(values_line[3]),Double.valueOf(values_line[4])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        initList();
        initialMessage = MessageLabel.getText();
        initialSliderValue = WagerSlider.getValue();
        ExpectedWinnings.setEditable(false);
        ExpectedPayout.setEditable(false);
        TotalWinnings.setEditable(false);
        initialLegValue = 1;

        RecordName.setText("Untitled");
        DateEntry.setValue(LocalDate.now());

        DateColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, LocalDate>("date"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, String>("name"));
        AmountWageredColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, Double>("amountWagered"));
        OddsColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, Double>("odds"));
        AmountWonColumn.setCellValueFactory(new PropertyValueFactory<BetRecord, Double>("amountWon"));

        TableView.setEditable(true);
        NameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableView.setItems(record);
        showTotalWinnings();

    }

    public void showTotalWinnings() {
        Double total = 0.0;
        Double wagers = 0.0;
        Double winnings = 0.0;
        for (int i= 0;i<TableView.getItems().size();i++){
            wagers = wagers+Double.valueOf(String.valueOf(TableView.getColumns().get(2).getCellObservableValue(i).getValue()));
            winnings = winnings+Double.valueOf(String.valueOf(TableView.getColumns().get(4).getCellObservableValue(i).getValue()));
        }
        total = winnings - wagers;
        if (total >0) {
            TotalWinnings.setStyle("-fx-text-fill: green");
        } else {
            TotalWinnings.setStyle("-fx-text-fill: red");
        }
        TotalWinnings.setText(String.format("%,.2f", total));

    }

    public void changeNameCellEvent(CellEditEvent editedCell) {
        BetRecord recordSelected = TableView.getSelectionModel().getSelectedItem();
        recordSelected.setName(editedCell.getNewValue().toString());

    }
    @FXML
    public void SaveFilePressed(ActionEvent event) {
        try {
            BufferedWriter outwriter = new BufferedWriter(new FileWriter(file));
            StringBuilder outstr = new StringBuilder("");

            for(BetRecord entries: record) {
                outstr.append(entries.getDate().toString() + "##" + entries.getName().toString() + "##" + entries.getAmountWagered().toString() + "##" + entries.getOdds().toString() + "##" + entries.getAmountWon().toString());
                String output = outstr.toString();
                outwriter.write(output);
                outwriter.newLine();
                outstr.setLength(0);

            }

            MessageLabel.setTextFill(Color.web("#00FF00"));
            MessageLabel.setText("File has been successfully saved.");
            outwriter.close();
        } catch (IOException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");
        }
    }
    @FXML
    void RemoveEntryPressed(ActionEvent event) {
        ObservableList<BetRecord> selectedRows, allRecords;
        allRecords = TableView.getItems();

        selectedRows = TableView.getSelectionModel().getSelectedItems();

        for (BetRecord record: selectedRows) {
            allRecords.remove(record);
        }
        MessageLabel.setTextFill(Color.web("#000000"));
        MessageLabel.setText("Entry Removed");
        showTotalWinnings();
    }

    @FXML
    void ResetButtonPressed(ActionEvent event) {
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
    void WonButtonPressed(ActionEvent event) {
        try {
            String entry = WagerEntry.getText();
            Double entrydouble = Double.parseDouble(entry);
            String odds = TotalOdds.getText();
            Double oddsdouble = Double.parseDouble(odds);
            String winnings = ExpectedWinnings.getText();
            Double winningsdouble = Double.parseDouble(winnings);

            BetRecord newRecord = new BetRecord(DateEntry.getValue(), RecordName.getText(), entrydouble, oddsdouble, winningsdouble);

            TableView.getItems().add(newRecord);
            showTotalWinnings();
        } catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");
        }
    }
    @FXML
    void LostButtonPressed(ActionEvent event) {
        try {
            String entry = WagerEntry.getText();
            Double entrydouble = Double.parseDouble(entry);
            String odds = TotalOdds.getText();
            Double oddsdouble = Double.parseDouble(odds);

            BetRecord newRecord = new BetRecord(DateEntry.getValue(), RecordName.getText(), entrydouble, oddsdouble, 0.00);

            TableView.getItems().add(newRecord);
            showTotalWinnings();
        } catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");
        }
    }
    @FXML
    void PayoutButtonPressed(ActionEvent event) {
        try {
            double WagerValue = Double.parseDouble(WagerEntry.getText());
            double OddsValue = Double.parseDouble(TotalOdds.getText());
            double SliderValue = WagerSlider.getValue();
            double OddsDecimal;
            if (OddsValue<0) {
                double OddsNegative = Math.abs(OddsValue);
               OddsDecimal = 100/OddsNegative +1;
            } else {
                OddsDecimal = OddsValue/100 +1;
            }
            double PayoutValue = (WagerValue*SliderValue)*OddsDecimal;
            double WinningsValue = PayoutValue-(WagerValue*SliderValue);
            WagerEntry.setText(String.valueOf(WagerValue*SliderValue));
            ExpectedPayout.setText(String.format("%.2f", PayoutValue));
            ExpectedWinnings.setText(String.format("%.2f", WinningsValue));
            WagerSlider.setValue(initialSliderValue);
            MessageLabel.setTextFill(Color.web("#000000"));
            MessageLabel.setText("Payout successfully calculated.");


        }catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");

        }

    }

    @FXML
    void ParlayButtonPressed(ActionEvent event) {
        try {
            double LegOneDecimal;
            if (Leg1.getText().isBlank()) {
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

            double ParlayDecimal = LegOneDecimal*LegTwoDecimal*LegThreeDecimal*LegFourDecimal*LegFiveDecimal*LegSixDecimal*LegSevenDecimal*LegEightDecimal*LegNineDecimal;
            double ParlayOdds;
            if (ParlayDecimal<2.0) {
               ParlayOdds = -100/(ParlayDecimal-1);
            }else{
                ParlayOdds = (ParlayDecimal-1)*100;
            }
            if (LegOneDecimal==1&&LegTwoDecimal==1&&LegThreeDecimal==1&&LegFourDecimal==1&&LegFiveDecimal==1&&LegSixDecimal==1&&LegSevenDecimal==1&&LegEightDecimal==1&&LegNineDecimal==1) {
                MessageLabel.setTextFill(Color.web("#a30000"));
                MessageLabel.setText("Error: No inputs in leg fields.");
            } else {
                double ParlayRounded = Math.round(ParlayOdds);
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
