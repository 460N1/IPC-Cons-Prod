package ipcmp_ConsumerProducer;
import java.util.Vector; 

class MessagePassing extends Thread { //Nga: Agon Hoxha
	static final int MAXQUEUE = 5;  //5 mesazhe maksimum mund te jene ne queue
									//klienti te merr mesazhe me rrall se 1 sekond
	@SuppressWarnings("rawtypes") //surpress per shkak te formatit te objektit message
	private Vector messages = new Vector(); 

	public void run() { 
		try { 
			while ( true ) { 
				putMessage(); //thirret funksioni putMessage
				sleep( 1000 ); //1 sekond thread wait state
            }
        }
		catch( InterruptedException e ) { } 
    }
	@SuppressWarnings("unchecked") //surpress per tipin e objektit message
	private synchronized void putMessage() 
		throws InterruptedException { 
		while ( messages.size() == MAXQUEUE ) //derisa te largohet mesazhi i kaluar nga queue
			wait(); //thread wait state
		messages.addElement(new java.util.Date().toString()); //dergon kohen dhe daten
		notify(); //ringjall threadin nga gjendja e pauzuar (nese ka qene ne te)
	}
    // Thirre prej klientit per me marr mesazhin
	public synchronized String getMessage() throws InterruptedException { 
		notify(); //ringjall threadin nga gjendja e pauzuar (nese ka qene ne te)
		while ( messages.size() == 0 ) //gjersa nuk i ka ardhur asnje mesazh
			wait(); //thread wait state
		String message = (String)messages.firstElement();  //Merre mesazhin e pare
		messages.removeElement( message );  //e largon mesazhin nga queue
		return message;  //e kthen mesazhin
	} 
} 

class Client extends Thread {  //Nga: Amire Gerguri
	MessagePassing server; //krijim i objektit server, te tipit Server ^
	Client(MessagePassing s) { //inicimi i klases Klienti me server ne konstruktor
		server = s; 
	} 
	public void run() { 
		try { 
			while ( true ) { 
				String message = server.getMessage(); //e merr mesazhin
				System.out.println("Mesazhi i marrur: " + message);  //e printon mesazhin e marr
				System.out.println("Tani duke pritur per mesazhin e radhes.");  //blla  blla
				sleep( 2000 );  //2 sekonda thread wait state
			} 
		}  
		catch(InterruptedException e) {System.out.println("Error najkun - kush e di ku."); } 
	} 
	public static void main(String args[]) { 
		MessagePassing server = new MessagePassing(); //krijimi i objektit server te tipit Server
		server.start();  //krijo thread te serverit
		new Client(server).start(); //konsumatori fillon te perpunoj te dhenat
	} 
} 