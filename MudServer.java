
package UP12;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
public class MudServer extends UnicastRemoteObject implements Mud.MailServerInterface {
    static int PORT = 2020;
    static ArrayList<Mud.MailClientInterface> ClientList;
    static ArrayList<Integer> clientslogs;
    private String userName;
    public MudServer(String name) throws RemoteException{
        this.userName=name;
        ClientList=new ArrayList<>();
        clientslogs=new ArrayList<>();
    }
    public static void main(String[] args) {
        try {
            MudServer server = new MudServer("Arthur_server");
            LocateRegistry.createRegistry(2020);
            System.setProperty("java.rmi.server.hostname","192.168.0.20");
            Naming.rebind("rmi://" + Mud.mudPrefix + ":" + PORT + "/" + server.userName,server);
            Date date = new Date();
            System.out.println(date);
            System.out.println("Server connecting!");
            while(true) {
            	Thread.sleep(300000);
            	for(int i=0;i<clientslogs.size();i++) {
            		clientslogs.set(i, 0);
            	}
            	System.out.println("проверка у сервака");
            }
            } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println("Usage: java FreeTimeServer <username> \n");
            System.exit(1);
        }
    }
    
    @Override
    public Mud.MailClientInterface getPerson(String name) throws RemoteException {
        Optional<Mud.MailClientInterface> optional = ClientList.stream().filter(p -> {
            try {
                return Objects.equals(p.getName(), name);
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
            return false;
        }).findFirst();
        System.out.println("Persons are:");
        for (Mud.MailClientInterface person : ClientList) {
            System.out.println(person.getName());
        }
        System.out.println("\n");
        return optional.orElse(null) ;
    }
    @Override
    public void delClient(Mud.MailClientInterface current) throws RemoteException{
    	 if(ClientList.indexOf(current)>=0) {
    	    	clientslogs.set(ClientList.indexOf(current), 1);
    	    }
        ClientList.remove(current);
    }
    @Override
    public String getPersons() throws RemoteException{
    	
        String res=("Clients:\n");
        for(Mud.MailClientInterface i:ClientList){
            res+=i.getName()+"\n";
        }
        return res;
    }
    @Override
    public boolean addPerson(Mud.MailClientInterface current) throws RemoteException {
    if(ClientList.indexOf(current)>=0) {
    	clientslogs.set(ClientList.indexOf(current), 1);
    }
    		
    	
        Optional<Mud.MailClientInterface> optional = ClientList.stream().filter(p -> {
            try {
                return Objects.equals(p.getName(), current.getName());
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
            return false ;
        }).findFirst();
        if (!optional.isPresent()) {
            ClientList.add(current);
            return true;
        }
        return false;
    }
    @Override
    public boolean printPerson(Mud.MailClientInterface current) throws RemoteException {
    	 if(ClientList.indexOf(current)>=0) {
    	    	clientslogs.set(ClientList.indexOf(current), 1);
    	    }
    	try {
			FileWriter nFile = new FileWriter("DATA.txt");
			 Date date = new Date();
			 nFile.append(current.getName().toString()+" "+date.toString()+"\n");
			 nFile.flush();
			 nFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    public boolean getStatus(Mud.MailClientInterface current) throws RemoteException {
    	if(clientslogs.get(ClientList.indexOf(current))==0) {
    		return false;
    	}
    	return true;
    }
    @Override
    public void sendMessage(Mud.MailClientInterface name, String message) throws RemoteException {
    	

    }
}
