# A Mass Mailer Java Application using Jakarta Mail API.
***

## The purpose of this program is to enable the user to send out bulk emails.
---
 
Download the Jakarta Mail and the Jakarta Activation JAR Files and configure in your repository from:\
[Download Jakarta Mail](https://eclipse-ee4j.github.io/mail/#Download_Jakarta_Mail_Release)\
[Download Jakarta Activation](https://mvnrepository.com/artifact/com.sun.activation/jakarta.activation/2.0.1)

---

The recipients.txt found in src\\resources\\ folder is where the recipient emails are stored (separated by a ',(comma)').\
The user can manually add new recipients in the recipient.txt file from within the program too.

---

The content.txt found in src\\resources\\ folder is where the text written in the email is stored.\
The user can override the text from within the program.

![Capture_MassMailer_1.PNG](https://github.com/04xRaynal/MassMailer_JavaMailAPI/blob/df03ce44fd090adc60e79517cdbe7f7de37a7329/Captured%20Images/Capture_MassMailer_1.PNG)

---


The user is required to add a Subject for the Email(s).
 
The user can choose if he wants to add CC, BCC or Attachments to his email(s). This is Optional.
 
The user then needs to enter his Email ID, from which he wishes to send the mail(s).

![Capture_MassMailer_2.PNG](https://github.com/04xRaynal/MassMailer_JavaMailAPI/blob/df03ce44fd090adc60e79517cdbe7f7de37a7329/Captured%20Images/Capture_MassMailer_2.PNG)

---

Once the user email is entered, the user has a choice to send each mail individually or to send 1 mail to all of the recipients.
If the user chooses the 1 mail(all) option, he can futher choose if he wants to hide the recipient emails while sending
(this is done by adding all emails to BCC which in turn hides the emails from the end user - the reciever).

![Capture_MassMailer_3.PNG](https://github.com/04xRaynal/MassMailer_JavaMailAPI/blob/df03ce44fd090adc60e79517cdbe7f7de37a7329/Captured%20Images/Capture_MassMailer_3.PNG)

---

The MassMailer_FakeSmtp Program is set up for the Fake SMTP Server, which can be downloaded at:\
[Download Fake Smtp Server](http://nilhcem.com/FakeSMTP/download.html)
 
---

Once the Fake SMTP Server is downloaded, start the server at 8080 Port,\
if you want to use a different port go to src\\resouces\\fake_smtp.properties\
and change the 'mail.smtp.port' to any port of your choice.

---

The MassMailer_Gmail Program is set up for the Google Mail (Gmail) Server,\ 
Can be Similarly set for other Email Providers.

---

Resolving AuthenticationFailedException\
(Google doesn't allow to send mails from unknown locations)
 
Click on this link and click on turn on radio button to allow users to send mail from unknown location.\ 
[Google less Secure Apps](https://www.google.com/settings/security/lesssecureapps)

---