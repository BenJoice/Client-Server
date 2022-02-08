package UP12;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Timer;

public class MudClient {
	public static boolean needtobreak=false;
    private static ArrayList<String> messages;
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        try {
            messages = new ArrayList<>();
            String hostname = "localhost";
            String username = "Arthur_server";
            Mud.MailServerInterface server = (Mud.MailServerInterface) Naming.lookup("rmi://" + hostname
                    + ":" + MudServer.PORT + "/" + username);
            System.out.println("You are connected to Server!");
            
            runUser(username, server);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Usage: java FreeTimeClient <host> <user>");
        System.exit(1);
    }
   
    private static void runUser(String username, Mud.MailServerInterface server) throws IOException, InterruptedException {
        String currentPersonName = welcome(username, server);
        Mud.MailClientInterface curname=server.getPerson(currentPersonName);
        String cmd;
        for(;;) {
        	if(needtobreak) {
        		
        		System.out.println("diskonected!");
                System.out.flush();
                System.exit(0);
        	}
            try {
                 try {
                 Thread.sleep(300);
                 } catch (InterruptedException ignored) {
                 }
                 cmd = getLine(">> ");

                switch (cmd) {
                    case "s":
                    	if(!server.getStatus(curname)) {
                    		System.out.println("время вашей сесси истекло");
                    		System.out.println("Bye!");
                            System.out.flush();
                    		 server.printPerson(curname);
                             System.exit(0);
                    	}
                        sendMesssage(server, currentPersonName);
                        break;
                    case "sh":
                    	if(!server.getStatus(curname)) {
                    		System.out.println("время вашей сесси истекло");
                    		System.out.println("Bye!");
                            System.out.flush();
                    		 server.printPerson(curname);
                             System.exit(0);
                    	}
                        showMessage(server,currentPersonName);
                        break;
                    case "u":
                    	if(!server.getStatus(curname)) {
                    		System.out.println("время вашей сесси истекло");
                    		System.out.println("Bye!");
                            System.out.flush();
                    		 server.printPerson(curname);
                             System.exit(0);
                    	}
                        showClients(server);
                        break;
                    case "help":
                    case "h":
                    	if(!server.getStatus(curname)) {
                    		System.out.println("время вашей сесси истекло");
                    		System.out.println("Bye!");
                            System.out.flush();
                    		 server.printPerson(curname);
                             System.exit(0);
                    	}
                        System.out.println(help);
                        break;
                    case "quit":
                    case "q":
                        delClient(server,currentPersonName);
                        System.out.println("Bye!");
                        System.out.flush();
                        server.printPerson(curname);
                        System.exit(0);
                     default:
                        System.out.println("Unknown command.  Try 'help'.\n");
                }
            } catch (Exception e) {
                System.out.println("Syntax or other error:");
                System.out.println(e);
                System.out.println("Try using the 'help' command.");
            }
        }
    }
    private static String welcome(String username, Mud.MailServerInterface server) throws IOException, InterruptedException {
        Mud.MailClientInterface currentPerson = null;
        do {
            String cmd = getLine("[" + username + "]:" + "Do you want to register or login?\n" +
                    "\tr : register\n" +
                    "\tl : login\n" +
                    ">> ");
            String name = getLine(">> Input your name: ");
            switch (cmd) {
                case "r": {
                	
                    PrintWriter out = new PrintWriter(System.out);
                    MudPerson current = new MudPerson(name, out, in);
                    if(server.printPerson(current)) {
                    	System.out.println("записано");
                    }
                    if (server.addPerson(current)) {
                        System.out.println(">> You are successful registered as \"" + name + "\"");
                        currentPerson = server.getPerson(name);
                    } else {
                        System.out.println(">> Name \"" + name + "\" is already used.");
                    }
                    break;
                }
                case "l": {
                	PrintWriter out = new PrintWriter(System.out);
                	MudPerson current = new MudPerson(name, out, in);
                	 if(server.printPerson(current)) {
                     	System.out.println("записано");
                     }
                    currentPerson = server.getPerson(name);
                    if (currentPerson != null) {
                        System.out.println(">> You are logged in as \"" + currentPerson.getName() + "\".");
                    } else {
                        System.out.println(">> No user with this name was found.");
                    }
                    break;
                }
                default: {
                    System.out.println(">> Incorrect key + \"" + cmd + "\"");
                    currentPerson = null;
                    break;
                }
            }
        } while (currentPerson == null);
            return currentPerson.getName();
    }
    private static String getLine(String prompt) throws InterruptedException {
        String line = null;
        do {
            try {
                System.out.print(prompt);
                System.out.flush();
                line = in.readLine();
                if (line != null) line = line.trim();
                } catch (Exception ignored) {

            }
        } while ((line == null) || (line.length() == 0));
        return line;
    }
    private  static  void delClient(Mud.MailServerInterface server,String personName)throws IOException, InterruptedException{
        Mud.MailClientInterface person = server.getPerson(personName);
        server.delClient(person);
    }
    private static void showClients(Mud.MailServerInterface server)throws IOException, InterruptedException{
        System.out.println(server.getPersons());
    }
    private static void showMessage(Mud.MailServerInterface server,String personName)throws IOException, InterruptedException{
       // String secondPersonName = getLine(">> Whose messages do you want to see ?\n" +
    //            ">> ");
        Mud.MailClientInterface person = server.getPerson(personName);
 //       Mud.MailClientInterface secondPerson = server.getPerson(secondPersonName);
        if (personName != null) {
            System.out.println(person.showMessage());
        }
        else {
            System.out.println(">> No user with this name was found.\n");
        }
        System.out.flush();
    }
    private static void sendMesssage(Mud.MailServerInterface server, String personName) throws IOException, InterruptedException {
        String secondPersonName = getLine(">> Who do you want to write a message to ?\n" +
                ">> ");        Mud.MailClientInterface person = server.getPerson(personName);
                Mud.MailClientInterface secondPerson = server.getPerson(secondPersonName);
                if (secondPerson != null) {
                    String message=personName+":  "+getLine(">> input your message\n" + ">> ");
                   // secondPerson.talk(message);
                    secondPerson.addLetter(message);
                }
                else {
                    System.out.println(">> No user with this name was found.\n");
                }
                System.out.flush();
    }
    private static final String help = "Commands:\n" +
            "s : send message\n" +
            "sh: show your message\n" +
            "u: show clients"+
            "help | h: display this message\n" +
            "q : quit\n";
}