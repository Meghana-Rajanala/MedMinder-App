package com.example.myapplication;

import static javax.mail.Transport.send;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class SendEmail extends AsyncTask<Void, Void, Boolean> {
    private String mRecipient;
    private String mSubject;
    private String mBody;

    public SendEmail(String recipient, String subject, String body) {
        mRecipient = recipient;
        mSubject = subject;
        mBody = body;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String host = "smtp.gmail.com";
        String username = "medminderappproject@gmail.com";
        String password = "picjktqrnqihoqjx";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("medminderappproject@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mRecipient));
            message.setSubject(mSubject);
            message.setText(mBody);

            send(message);

            return true;

        } catch (MessagingException e) {
            Log.e("Email", "Error sending email", e);

            return false;
        }
    }

}