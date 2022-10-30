package com.example.javaendassignment2022;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    private Database db;
    private ObservableList<Member> members;
    private ObservableList<Item> items;
    @FXML
    private TableView<Member> tableViewMembers;
    @FXML
    private TableView<Item> tableViewItems;

    @FXML
    private TableColumn<Member, Integer> colId;
    @FXML
    private TableColumn<Member, String> colFirstName, colLastName;
    @FXML
    private TableColumn<Member, LocalDate> colBirthDate;
    @FXML
    private TableColumn<Item, Integer> colItemCode;
    @FXML
    private TableColumn<Item, String> colTitle, colAuthor;
    @FXML
    private TableColumn<Item, String> colAvailability;
    @FXML
    private VBox vBoxAddMember, vBoxMember, vBoxEditMember, vBoxItems, vBoxAddItem, vBoxEditItem;
    @FXML
    private Label welcomeUserLabel, labelLendItem, labelreceiveItem;
    @FXML
    private TextField textFieldFirstName, textFieldLastName, textFieldEditFirstName, textFieldEditLastName, textFieldTitle, textFieldAuthor, textFieldLendItemCode, textFieldMemberId, textFieldReceiveItemCode, textFieldEditTitle, textFieldEditAuthor, textFieldSearchItems, textFieldSearchMembers;
    @FXML
    private DatePicker datePickerBirthDate, datePickerEditBirthDate;
    @FXML
    private RadioButton rButtonYes, rButtonNo, rButtonYesEditItem, rButtonNoEditItem;
    @FXML
    private Button buttonEditItem, buttonDeleteItem, buttonEditMember, buttonDeleteMember;

    private Item lendItem, receiveItem, selectedItem;

    private Member lendMember = null;
    private FilteredList<Item> filteredListItem;

    private FilteredList<Member> filteredListMember;

    private Member selectedMember;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        db = new Database();

        File itemsFile = new File("items.dat");
        File membersFile = new File("members.dat");
        db.checkFileAvailability(itemsFile);
        db.checkFileAvailability(membersFile);

        members = FXCollections.observableArrayList(db.getMembers());
        items = FXCollections.observableArrayList(db.getItems());

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("Availability"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
        labelLendItem.setText("");
        labelreceiveItem.setText("");

        filteredListItem = new FilteredList<>(items, b -> true);
        filteredListMember = new FilteredList<>(members, b -> true);
        searchFilter(filteredListItem, textFieldSearchItems, tableViewItems);
        searchFilter(filteredListMember, textFieldSearchMembers, tableViewMembers);

        disableButtonWhenNothingIsSelected(buttonEditItem, tableViewItems);
        disableButtonWhenNothingIsSelected(buttonDeleteItem, tableViewItems);
        disableButtonWhenNothingIsSelected(buttonEditMember, tableViewMembers);
        disableButtonWhenNothingIsSelected(buttonDeleteMember, tableViewMembers);
    }

    private void disableButtonWhenNothingIsSelected(Button button, TableView tableView) {
        button.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
    }

    private void searchFilter(FilteredList list, TextField textField, TableView tableView) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            list.setPredicate(object -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                if (object.getClass() == Item.class) {
                    if (((Item) object).getTitle().toLowerCase().contains(newValue.toLowerCase())) {return true;}
                    else if (((Item) object).getAuthor().toLowerCase().contains(newValue.toLowerCase())) {return true;}
                    else return false;
                }
                else if (object.getClass() == Member.class) {
                    if (((Member) object).getFirstName().toLowerCase().contains(newValue.toLowerCase())) {return true;}
                    else if (((Member) object).getLastName().toLowerCase().contains(newValue.toLowerCase())) {return true;}
                    else return false;
                }
                else
                    return false;
            });
        });

        tableView.setItems(list);
    }

    //Collection tab

    private void unSelectAndDisableItemsButtons() {
        tableViewItems.getSelectionModel().select(null);

        disableButtonWhenNothingIsSelected(buttonEditItem, tableViewItems);
        disableButtonWhenNothingIsSelected(buttonDeleteItem, tableViewItems);
    }
    private void updateItemsTableView() {
        items = FXCollections.observableArrayList(db.getItems());
        tableViewItems.refresh();
        filteredListItem = new FilteredList<>(items, b -> true);
        searchFilter(filteredListItem, textFieldSearchItems, tableViewItems);
        db.saveItems();

        unSelectAndDisableItemsButtons();
    }

    private boolean getAvailabilityWithRadioButton(RadioButton radioButton) {
        if (radioButton.isSelected()) {
            return true;
        }
        else {
            return false;
        }
    }

    @FXML
    private void onClickLendItemButton() {
        try {
            for (Item item : items) {
                if (Integer.parseInt(textFieldLendItemCode.getText()) == item.getItemCode()) {lendItem = item;}
            }

            for (Member member : members) {
                if (Integer.parseInt(textFieldMemberId.getText()) == member.getId()) {lendMember = member;}
            }
            if (lendItem.getAvailability().equals("Yes")) {
                db.lendItem(lendItem, lendMember);
                updateItemsTableView();
                labelLendItem.setStyle("-fx-text-fill: green");
                labelLendItem.setText("Item has been successfully lent");
                textFieldLendItemCode.setText("");
                textFieldMemberId.setText("");
            }
            else if (lendItem.getAvailability().equals("No")) {
                labelLendItem.setStyle("-fx-text-fill: red");
                labelLendItem.setText("This item has already been lent to " + lendItem.getMember().getFirstName());
            }
            else {
                labelLendItem.setStyle("-fx-text-fill: red");
                labelLendItem.setText("Incorrect item code or member id entered");
            }
        }
        catch (Exception e) {
            labelLendItem.setStyle("-fx-text-fill: red");
            labelLendItem.setText("Please fill in all the text-fields first");
        }
    }

    @FXML
    private void onClickReceiveItemButton() {
        try {
            for (Item item : items) {
                if (Integer.parseInt(textFieldReceiveItemCode.getText()) == item.getItemCode()) {receiveItem = item;}
            }

            long lendDuration = Duration.between(receiveItem.getLendDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

            if (lendDuration > 21  && receiveItem.getAvailability().equals("No")) {
                db.receiveItem(receiveItem);
                updateItemsTableView();
                labelreceiveItem.setStyle("-fx-text-fill: red");
                labelreceiveItem.setText("This item is " + (lendDuration - 21) + " days late received");
                textFieldReceiveItemCode.setText("");
            }
            else if (receiveItem.getAvailability().equals("No")) {
                db.receiveItem(receiveItem);
                updateItemsTableView();
                labelreceiveItem.setStyle("-fx-text-fill: green");
                labelreceiveItem.setText("Item has been successfully received");
                textFieldReceiveItemCode.setText("");
            }
            else {
                labelreceiveItem.setStyle("-fx-text-fill: red");
                labelreceiveItem.setText("This item has not been lent");
            }
        }
        catch (Exception e) {
            labelreceiveItem.setStyle("-fx-text-fill: red");
            labelreceiveItem.setText("Please fill in the text-field first");
        }
    }

    @FXML
    private void onClickAddItemButton1() {
        vBoxAddItem.setVisible(true);
        vBoxItems.setVisible(false);
        textFieldTitle.clear();
        textFieldAuthor.clear();
        rButtonYes.setSelected(false);
        rButtonNo.setSelected(false);
    }

    @FXML
    private void onClickAddItemButton2() {
        try {
            db.addItems(getAvailabilityWithRadioButton(rButtonYes), textFieldTitle.getText(), textFieldAuthor.getText(), null, null);
            vBoxItems.setVisible(true);
            vBoxAddItem.setVisible(false);
            updateItemsTableView();
        } catch (Exception e) {} // ignore when not every field is filled
    }

    @FXML
    private void onClickCancelAddItemButton() {
        vBoxItems.setVisible(true);
        vBoxAddItem.setVisible(false);

        unSelectAndDisableItemsButtons();
    }

    @FXML
    private void onClickEditItemButton() {
        selectedItem = tableViewItems.getSelectionModel().getSelectedItem();
        textFieldEditTitle.setText(selectedItem.getTitle());
        textFieldEditAuthor.setText(selectedItem.getAuthor());
        if (selectedItem.getAvailability().equals("Yes")) {
            rButtonYesEditItem.setSelected(true);
        }
        else {
            rButtonNoEditItem.setSelected(true);
        }
        vBoxItems.setVisible(false);
        vBoxEditItem.setVisible(true);
    }

    @FXML
    private void onClickUpdateItemButton() {
        try {
            db.editItem(selectedItem, getAvailabilityWithRadioButton(rButtonYesEditItem), textFieldEditTitle.getText(), textFieldEditAuthor.getText());
            vBoxItems.setVisible(true);
            vBoxEditItem.setVisible(false);
            updateItemsTableView();
        } catch (Exception e) {} // ignore when not every field is filled
    }

    @FXML
    private void onClickCancelEditItemButton() {
        vBoxItems.setVisible(true);
        vBoxEditItem.setVisible(false);

        unSelectAndDisableItemsButtons();
    }

    @FXML
    private void onClickDeleteItemButton() {
        selectedItem = tableViewItems.getSelectionModel().getSelectedItem();
        db.removeItem(selectedItem);
        updateItemsTableView();
    }

    //Members tab

    private void unSelectAndDisableMembersButtons() {
        tableViewMembers.getSelectionModel().select(null);

        disableButtonWhenNothingIsSelected(buttonEditMember, tableViewMembers);
        disableButtonWhenNothingIsSelected(buttonDeleteMember, tableViewMembers);
    }
    private void updateMembersTableView() {
        members = FXCollections.observableArrayList(db.getMembers());
        tableViewMembers.refresh();

        filteredListMember = new FilteredList<>(members, b -> true);
        searchFilter(filteredListMember, textFieldSearchMembers, tableViewMembers);
        db.saveMembers();

        unSelectAndDisableMembersButtons();
    }

    @FXML
    private void onClickAddMemberButton() {
        vBoxAddMember.setVisible(true);
        vBoxMember.setVisible(false);
        textFieldFirstName.clear();
        textFieldLastName.clear();
        datePickerBirthDate.getEditor().clear();
    }

    @FXML
    private void onClickAddMemberButton2() {
        try {
            db.addMember(textFieldFirstName.getText(), textFieldLastName.getText(), datePickerBirthDate.getValue());
            vBoxMember.setVisible(true);
            vBoxAddMember.setVisible(false);
            updateMembersTableView();
        } catch (Exception e) {} // ignore when not every field is filled
    }

    @FXML
    private void onClickCancelAddMemberButton() {
        vBoxMember.setVisible(true);
        vBoxAddMember.setVisible(false);
        unSelectAndDisableMembersButtons();
    }

    @FXML
    private void onClickCancelEditMemberButton() {
        vBoxMember.setVisible(true);
        vBoxEditMember.setVisible(false);
        unSelectAndDisableMembersButtons();
    }

    @FXML
    private void onClickEditMemberButton() {
        selectedMember = tableViewMembers.getSelectionModel().getSelectedItem();
        textFieldEditFirstName.setText(selectedMember.getFirstName());
        textFieldEditLastName.setText(selectedMember.getLastName());
        datePickerEditBirthDate.setValue(selectedMember.getBirthDateInLocalDate());
        vBoxEditMember.setVisible(true);
        vBoxMember.setVisible(false);
    }
    @FXML
    private void onClickUpdateMemberButton() {
        try {
            db.editMembers(selectedMember, textFieldEditFirstName.getText(), textFieldEditLastName.getText(), datePickerEditBirthDate.getValue());
            vBoxMember.setVisible(true);
            vBoxEditMember.setVisible(false);
            updateMembersTableView();
        } catch (Exception e) {} // ignore when not every field is filled
    }

    @FXML
    private void onClickDeleteMemberButton() {
        selectedMember = tableViewMembers.getSelectionModel().getSelectedItem();
        db.removeMember(selectedMember);
        updateMembersTableView();
    }

    public void displayWelcomeAndName(String message) {
        welcomeUserLabel.setText("Welcome " + message);
    }
}
