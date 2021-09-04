package gameEngine.pages;

import gameEngine.AlertBox;
import gameEngine.components.Loans;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LoansPage extends Page {

    private final Loans loans;
    private final Stage stage;

    public LoansPage(Stage stage, Loans loans) {
        this.loans = loans;
        this.stage = stage;
    }

    public Scene setSceneAbleLoans(MainPage mainPage) {
        BorderPane paneAbleLoans = new BorderPane();
        HBox hbox = new HBox(2);
        VBox vbox = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER_LEFT);
        List<String> availableLoans = this.loans.getAvailableLoans();
        for (String al : availableLoans){
            vbox.getChildren().add(getStandardLabel(al));
        }
        hbox.getChildren().add(vbox);
        paneAbleLoans.setTop(getLabel("Available Loans",paneAbleLoans));
        paneAbleLoans.setCenter(hbox);

        Label typeLbl = getStandardLabel("Loans type");
        TextField typeField = newTextField();

        HBox bottom = backMainButton(mainPage);
        Button requestButton = getButton("Request");
        requestButton.setOnAction(e -> {
            String type = typeField.getText().strip();
            if (type != "") {
                try {
                    List<String> requestedLoans = this.loans.requestLoans(type);
                    stage.setScene(setSuccessfulLoansRequest(mainPage, requestedLoans));
                }
                catch (IllegalStateException se) {
                    AlertBox.display("Warning", se.getMessage());
                }
            }
            else {
                AlertBox.display("Warning", "Please input request loans type.");
            }
        });
        bottom.getChildren().addAll(typeLbl,typeField,requestButton);
        paneAbleLoans.setBottom(bottom);
        paneAbleLoans.setStyle("-fx-background-color: #3e455c");
        return new Scene(paneAbleLoans, 800, 450);
    }

    public Scene setSuccessfulLoansRequest(MainPage mainPage, List<String> requestedLoans) {
        BorderPane paneSuccessRequestLoans = new BorderPane();
        HBox hbox = new HBox(2);
        VBox vbox = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER_LEFT);
        for (String rl : requestedLoans){
            vbox.getChildren().add(getStandardLabel(rl));
        }
        hbox.getChildren().add(vbox);
        paneSuccessRequestLoans.setStyle("-fx-background-color: #3e455c");
        paneSuccessRequestLoans.setTop(getLabel("Request Loans Successful",paneSuccessRequestLoans));
        paneSuccessRequestLoans.setCenter(hbox);

        paneSuccessRequestLoans.setBottom(backMainButton(mainPage));
        return new Scene(paneSuccessRequestLoans, 800, 450);
    }

    public Scene setSceneYourLoans(MainPage mainPage) {
        BorderPane paneYourLoans = new BorderPane();
        HBox hbox = new HBox(2);
        VBox vbox = new VBox(2);
        VBox center = new VBox(10);
        hbox.setAlignment(Pos.CENTER);
        vbox.setAlignment(Pos.CENTER_LEFT);
        center.setAlignment(Pos.CENTER);
        List<String> yourLoans = this.loans.getYourLoans();
        if (yourLoans.size() == 0) {
            vbox.getChildren().add(getStandardLabel("You have not requested any loans yet."));
            hbox.getChildren().add(vbox);
        }
        else {
            HBox idBox = new HBox(10);
            idBox.setAlignment(Pos.CENTER);
            VBox loansIdsBox = new VBox(10);
            loansIdsBox.setAlignment(Pos.CENTER);
            List<String> loansIds = this.loans.getYourLoansId();
            if (loansIds.size() == 0) {
                Label loansIdsLabel = getStandardLabel("You don't have any loans.");
                loansIdsBox.getChildren().add(loansIdsLabel);
            }
            else {
                loansIdsBox.getChildren().addAll(getStandardLabel(" "),
                                            getStandardLabel("Your loans' ids are listed below."));
                for (String id: loansIds) {
                    TextField loansIdField = getTextFieldWithText(id);
                    setTextField(loansIdField);
                    loansIdsBox.getChildren().add(loansIdField);
                }
                loansIdsBox.getChildren().add(getStandardLabel(" "));
            }
            Label loansIdLbl = getStandardLabel("Loan id to pay off");
            TextField loansIdField = newTextField();
            idBox.getChildren().addAll(loansIdLbl,loansIdField);
            Button payOffButton = getButton("Pay off");
            payOffButton.setOnAction(e -> {
                String loansId = loansIdField.getText().strip();
                if (loansId != "") {
                    try {
                        List<String> paidLoans = this.loans.payOffLoans(loansId);
                        stage.setScene(this.payLoansSuccess(mainPage, paidLoans));
                    }
                    catch (IllegalStateException se) {
                        AlertBox.display("Warning", se.getMessage());
                    }
                }
                else {
                    AlertBox.display("Warning", "Please input Loans id");
                }
            });
            for (String al : yourLoans){
                vbox.getChildren().add(getStandardLabel(al));
            }
            vbox.getChildren().addAll(loansIdsBox,idBox);
            center.getChildren().addAll(vbox,payOffButton);
            hbox.getChildren().add(center);
        }
        paneYourLoans.setTop(getLabel("Your Loans",paneYourLoans));
        paneYourLoans.setCenter(hbox);

        paneYourLoans.setBottom(backMainButton(mainPage));
        paneYourLoans.setStyle("-fx-background-color: #3e455c");
        return new Scene(paneYourLoans, 800, 450);
    }

    public Scene payLoansSuccess(MainPage mainPage, List<String> paidLoans) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        HBox hbox = new HBox(2);
        VBox content = new VBox(2);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(backMainButton(mainPage),
                                    getStandardLabel(" "),
                                    getStandardLabel("You have paid your loans successfully"),
                                    getStandardLabel("Your information is updated"),
                                    getStandardLabel(" "));
        for (String pl : paidLoans){
            content.getChildren().add(getStandardLabel(pl));
        }
        content.getChildren().add(backMainButton(mainPage));
        hbox.getChildren().add(content);

        scrollPane.setContent(hbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background:#3e455c;");
        return new Scene(scrollPane, 800, 450);
    }

    private HBox backMainButton(MainPage mainPage) {
        HBox backButton = new HBox();
        backButton.setSpacing(10.0);
        backButton.setAlignment(Pos.CENTER);
        Button backMain = getButton("Back");
        backMain.setOnAction(e -> stage.setScene(mainPage.getSceneMain()));
        backButton.getChildren().add(backMain);
        return backButton;
    }
}
