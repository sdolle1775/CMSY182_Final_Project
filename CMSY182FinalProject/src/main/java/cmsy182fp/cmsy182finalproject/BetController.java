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
import java.lang.Math;
import java.time.LocalDate;
import java.time.Month;

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

    public void initialize() {
        initialMessage = MessageLabel.getText();
        initialSliderValue = WagerSlider.getValue();
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

        TableView.setItems(getRecords());
    }

    public ObservableList<BetRecord> getRecords() {
        ObservableList<BetRecord> record = FXCollections.observableArrayList();
        record.add(new BetRecord(LocalDate.of(2022, Month.OCTOBER, 20),"Example Entry 1",350.50, 200.0, 701.00  ));
        record.add(new BetRecord(LocalDate.of(2022, Month.NOVEMBER, 13),"Example Entry 2",100.00, -150.0, 66.67  ));
        record.add(new BetRecord(LocalDate.of(2022, Month.DECEMBER, 1),"Example Entry 3",80.75, 100.0, 0.00  ));

        return record;
    }

    public void changeNameCellEvent(CellEditEvent editedCell) {
        BetRecord recordSelected = TableView.getSelectionModel().getSelectedItem();
        recordSelected.setName(editedCell.getNewValue().toString());

    }
    private class ClearOutputChangeListener<T> implements  ChangeListener<T> {
        @Override
        public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue) {
            clearCalculatedControls();
        }
    }

    private void clearCalculatedControls() {
        MessageLabel.setText(initialMessage);
        WagerEntry.clear();
        WagerSlider.setValue(initialSliderValue);
        TotalOdds.clear();
        ExpectedPayout.clear();
        ExpectedWinnings.clear();
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
            double ParlayRounded = Math.round(ParlayOdds);
            TotalOdds.setText(String.valueOf(ParlayRounded));
            MessageLabel.setTextFill(Color.web("#000000"));
            MessageLabel.setText("Parlay successfully calculated.");


        }catch (NumberFormatException e) {
            MessageLabel.setTextFill(Color.web("#a30000"));
            MessageLabel.setText("Parsing Error: Check your inputs.");

        }
    }



}
