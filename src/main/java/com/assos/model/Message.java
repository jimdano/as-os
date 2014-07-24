package com.assos.model;

import java.util.Date;

/**
 * Created by jimmy on 11/06/2014.
 */
public class Message {

    private String senderId;

    private String titre;
    private String message;

    private Date dateEnvoi;

    public Message(String senderId, String titre, String message, Date dateEnvoi){
        this.senderId = senderId;
        this.message = message;
        this.titre = titre;
        this.dateEnvoi = dateEnvoi;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
