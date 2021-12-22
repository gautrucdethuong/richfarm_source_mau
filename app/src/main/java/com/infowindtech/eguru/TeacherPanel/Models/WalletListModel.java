package com.infowindtech.eguru.TeacherPanel.Models;


public class WalletListModel {

    private String name;
    private String amount;
    private String withdrawal_id;
    private String status;
    private String selected_method;
    private String created_at;
    private String transaction;
    private String currency;

    public WalletListModel()
    {

    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdrawal_id() {
        return withdrawal_id;
    }

    public void setWithdrawal_id(String withdrawal_id) {
        this.withdrawal_id = withdrawal_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelected_method() {
        return selected_method;
    }

    public void setSelected_method(String selected_method) {
        this.selected_method = selected_method;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
