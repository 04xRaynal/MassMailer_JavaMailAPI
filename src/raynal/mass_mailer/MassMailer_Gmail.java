/*
 * A Mass Mailer Java Application using Jakarta Mail API.
 * The purpose of this program is to enable the user to send out bulk emails.
 * 
 * Download the Jakarta Mail and the Jakarta Activation JAR Files and configure in your repository from:
 * https://eclipse-ee4j.github.io/mail/#Download_Jakarta_Mail_Release
 * https://mvnrepository.com/artifact/com.sun.activation/jakarta.activation/2.0.1
 * 
 * 
 * The recipients.txt found in src\resources\ folder is where the recipient emails are stored (separated by a ',(comma)').
 * The user can manually add new recipients in the recipient.txt file from within the program too.
 * 
 * The content.txt found in src\resources\ folder is where the text written in the email is stored.
 * The user can override the text from within the program.
 * 
 * The user needs to enter his Gmail ID and Password, from which he wishes to send the mail(s).
 * 
 * The user is required to add a Subject for the Email(s).
 * 
 * The user can choose if he wants to add CC, BCC or Attachments to his email(s). This is Optional.
 * 
 * The user has a choice to send each mail individually or to send 1 mail to all of the recipients.
 * If the user chooses the 1 mail(all) option, he can futher choose if he wants to hide the recipient emails while sending
 * (this is done by adding all emails to BCC which in turn hides the emails from the end user - the reciever).
 * 
 * 
 * This Program is set up for the Google Mail (Gmail) Server, 
 * Can be Similarly set for other Email Providers.
 * 
 * 
 * Resolving AuthenticationFailedException
 * (Google doesn't allow to send mails from unknown locations)
 * 
 * Click on this link and click on turn on radio button to allow users to send mail from unknown location. 
 * https://www.google.com/settings/security/lesssecureapps
 * 
 * 
 * @author - 04xRaynal
 */
package raynal.mass_mailer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class MassMailer_Gmail {
	Properties properties;
	Session session;
	Message message;
	private ArrayList<File> files;
	
	public MassMailer_Gmail() {
		try {
			properties = System.getProperties();
			FileInputStream fis = new FileInputStream("src\\resources\\gmail.properties");
			properties.load(fis);
			fis.close();
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter your Google Email:");
			String username = sc.nextLine();
			System.out.println("Enter password:");
			String password = sc.nextLine();
			
			properties.setProperty("mail.smtp.username", username);
			properties.setProperty("mail.smtp.password", password);
			
			session = Session.getDefaultInstance(properties, new jakarta.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});								//Password is authenticated for the entered email
			
			files = new ArrayList<>();											//List to store the Attachment Files if added
			List<String> recipientEmails = new ArrayList<>();					//List to store the recipient emails
			List<String> ccEmailsList = new ArrayList<>();						//List to store the CC Emails if added
			List<String> bccEmailsList = new ArrayList<>();						//List to store the BCC Emails if added
			
			try {   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   }
			catch (ClassNotFoundException e) {}
	        catch (InstantiationException e) {}
	        catch (IllegalAccessException e) {}
	        catch (UnsupportedLookAndFeelException e) {}        //Refines the look of the ui (For the JFileChooser)
			
			final JFileChooser fileChooser = new javax.swing.JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			int sentCount = 0;
			
			System.out.println("\n\nEmail Recipients are taken from recipients.txt file found in the src\\resources\\ folder.\n"
					+ "You can also manually add recipient emails in the recipients.txt file.");
			
			System.out.println("Do you want to manually enter recipient emails?\n"
					+ "Press Y for Yes or N for No");
			String manualEnter = sc.nextLine();

			switch(manualEnter) {
				case ("Y"):
				case ("y"):
					String manualInput = null;
					
					do {
						System.out.println("Enter a recipient email, enter 'STOP' to stop:");
						manualInput = sc.nextLine();
						recipientEmails.add(manualInput);
					} while(!manualInput.toLowerCase().equals("stop"));
					
					recipientEmails.remove(recipientEmails.size() - 1);							//"stop" is removed from the list
					
					FileWriter fw = new FileWriter(new File("src\\resources\\recipients.txt"), true);			//true denotes [append - true]
					for(String email: recipientEmails) {
						fw.write(email);
						fw.write(",");
					}
					fw.flush();
					fw.close();
					break;
				
				case ("N"):
				case ("n"):
					StringBuilder sbEmail = new StringBuilder();
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("src\\resources\\recipients.txt")));
					int readByte;
					while((readByte = bis.read()) != -1) {
						sbEmail.append((char)readByte);
					}
					bis.close();
					
					String[] emailArray = sbEmail.toString().split(",");				//Values from the StringBuilder are stored in a String, with commas removed
					for(String email: emailArray)
						recipientEmails.add(email);
					break;
				
				default: System.out.println("Invalid Input! Program terminated.");
			}
			
			System.out.println("Enter a Subject for your Mail:");
			String subject = sc.nextLine();
			
			System.out.println("Press 'C' to use content.txt or 'X' to input text");
			String contentInput = sc.nextLine();
			StringBuilder contentSB = new StringBuilder();
			
			switch(contentInput) {
				case ("C"):
				case ("c"):
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("src\\resources\\content.txt")));
					int contentRead;
					while((contentRead = bis.read()) != -1) {
						contentSB.append((char)contentRead);
					}
					break;
				
				case ("X"):
				case ("x"):
				default:
					String inputText = null;
					do {
						System.out.println("Enter Line of Text, input 'STOP' to stop:");
						inputText = sc.nextLine();
						contentSB.append(inputText + "\n");
					} while(! (inputText.toLowerCase().equals("stop")));
					
					contentSB.delete(contentSB.length() - 5, contentSB.length());       //deleting (length - 5) from the end to remove "stop\n"
			}
			
			System.out.println("Do you want to add CC, BCC or add File Attachments?\n"
					+ "Input 'Y' for Yes or 'N' for No");
			String attachmentInput = sc.nextLine();
			
			if(attachmentInput.toLowerCase().equals("y")) {
				System.out.println("Do you want to add emails in CC ?\n"
						+ "Press 'Y for Yes or 'N' for No.");
				String ccInput = sc.nextLine();
				String ccInputEmail = "";
				if(ccInput.equals("Y") || ccInput.equals("y")) {
					do {
						System.out.println("Input an email for CC, Enter STOP to stop");
						ccInputEmail = sc.nextLine();
						ccEmailsList.add(ccInputEmail);
					}while(! ccInputEmail.toLowerCase().equals("stop"));
					
					ccEmailsList.remove(ccEmailsList.size() - 1);					//removing "stop" from the list
				}
				
				
				System.out.println("Do you want to add emails in BCC ?\n"
						+ "Press 'Y for Yes or 'N' for No.");
				String bccInput = sc.nextLine();
				String bccInputEmail = "";
				if(bccInput.equals("Y") || bccInput.equals("y")) {
					do {
						System.out.println("Input an email for BCC, Enter STOP to stop");
						bccInputEmail = sc.nextLine();
						bccEmailsList.add(bccInputEmail);
					}while(! bccInputEmail.toLowerCase().equals("stop"));
					
					bccEmailsList.remove(bccEmailsList.size() - 1);					//removing "stop" from the list
				}
				
				String attachInput;
				do {
					System.out.println("Do you want to add File Attachments?\n"
							+ "Press 'Y for Yes or 'N' for No.");
					attachInput = sc.nextLine();
					if(attachInput.equals("Y") || attachInput.equals("y")) {
						int fcReturnValue = fileChooser.showOpenDialog(null);
						if(fcReturnValue == JFileChooser.APPROVE_OPTION) {
							files.add(fileChooser.getSelectedFile());
						}
					}
				} while(! attachInput.toLowerCase().equals("n"));
			}
			
			message = new MimeMessage(session);
			try {
				message.setFrom(new InternetAddress(username));
				
				message.setSubject(subject);
				message.setSentDate(new Date());
				
				//If CC Emails were added above, adding them in mail
				if(ccEmailsList.size() > 0) {
					StringBuilder sbCcEmails = new StringBuilder();
					for(String ccEmail: ccEmailsList)
						sbCcEmails.append(ccEmail + ",");
					
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(sbCcEmails.toString()));
				}
				
				//If BCC Emails were added above, adding them in mail
				if(bccEmailsList.size() > 0) {
					StringBuilder sbBccEmails = new StringBuilder();
					for(String bccEmail: bccEmailsList)
						sbBccEmails.append(bccEmail + ",");
					
					message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(sbBccEmails.toString()));
				}
				
				
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				Multipart multipart = new MimeMultipart();
				
				messageBodyPart.setContent(contentSB.toString(),"text/plain");
				multipart.addBodyPart(messageBodyPart);
				
				//If Attachments were added above, adding them in mail
				if(files.size() > 0) {
					for(File file: files) {
						MimeBodyPart messageBodyPart_file = new MimeBodyPart();
						messageBodyPart_file.attachFile(file);
						multipart.addBodyPart(messageBodyPart_file);
					}
				}
				message.setContent(multipart);
				
				String sendPref;
				do {
					System.out.println("Enter 'IND' to mail all recipients individually.\n"
							+ "Or enter 'ALL' to include All recipients in a single mail.");
					sendPref = sc.nextLine();
					
				} while(! (sendPref.toLowerCase().equals("all") || sendPref.toLowerCase().equals("ind")));
					
				long startTime = System.currentTimeMillis();
				
				switch(sendPref.toLowerCase()) {
					case ("ind"):
						//Sending emails individually
						for(String recipientEmail: recipientEmails) {
							message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
												
							Transport.send(message);
							sentCount++;
						}
						break;
					
					case ("all"):
						StringBuilder sbEmailsList = new StringBuilder();
						for(String recipientEmail: recipientEmails)
							sbEmailsList.append(recipientEmail + ",");
						
						System.out.println("Do you want to hide the recipients Email Address?\n"
								+ "Enter 'Y' for YES or 'N' for NO");
						String allPref = sc.nextLine();
						
						if(allPref.toLowerCase().equals("y")) {
							message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(sbEmailsList.toString()));				//with BCC recipients name gets hidden
						}
						else {
							message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(sbEmailsList.toString()));
						}
						
						Transport.send(message);
						sentCount++;
						break;
					
					default: System.out.println("Invalid Input! Enter Again.");
				}
				
				long endTime = System.currentTimeMillis();
				System.out.println("Total Time taken to send mails: " + (endTime-startTime)/1000 + "seconds");
				
			}
			catch(AddressException ex) {
				ex.printStackTrace();
			}
			catch(MessagingException ex) {
				ex.printStackTrace();
			}
			finally {
				sc.close();
			}
			
			System.out.println("Amount of E-mails sent: " + sentCount);
			
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MassMailer_Gmail();
	}

}
