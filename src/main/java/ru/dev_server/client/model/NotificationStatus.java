package ru.dev_server.client.model;

/**Sms notification send status.*/
public enum NotificationStatus {
    /** waiting to send to smsc.*/
    WAITING,
    /** to smsc.*/
    SENT,
    /**received by client.*/
    RECEIVED,
    /** Error when sending sms.*/
    ERROR,

    /**for long messages. different status of parts.*/
    DIFFERENT,

    /**message expired.*/
    EXPIRED
}
