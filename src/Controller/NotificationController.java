package Controller;

import Interfaces.INotificationService;
import Services.EmailNotificationService;
import Services.SMSNotificationService;

public class NotificationController {
    INotificationService _service;

    public void SendEmailNotification(String EmailId){
        _service=new EmailNotificationService();
        _service.SendMessage(EmailId);
    }

    public void SendSMS(String mobileNumber){
        _service=new SMSNotificationService();
        _service.SendMessage(mobileNumber);
    }

}
