package com.asset.ccat.gateway.models.customer_care;

/**
 *
 * @author nour.ihab
 */
public class GetAllNotePadModel {

    private String operator;
    private Long date;
    private String note;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
