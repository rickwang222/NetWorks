/* Spring 2020 CSci4211: Introduction to Computer Networks
** This program serves as the server of DNS query.
** Written in Java. */

//OH Questions
//1, ipselection
//2, log server 

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;



class DNSServer {
	public static void main(String[] args) throws Exception {
		int port = 8990;
		ServerSocket sSock = null;

		try {
			sSock = new ServerSocket(port); // Try to open server socket 5001.
		} catch (Exception e) {
			System.out.println("Error: cannot open socket");
			System.exit(1); // Handle exceptions.
		}

		System.out.println("Server is listening...");
		new monitorQuit().start(); // Start a new thread to monitor exit signal.

		while (true) {
			new dnsQuery(sSock.accept()).start(); //question: keeps calling new thread without new connection request?
		}
	}
}





class dnsQuery extends Thread {
	Socket sSock = null;
    
	dnsQuery(Socket sSock) {
    	this.sSock = sSock;
    }

	//funtion for writing to log file
	public void writeLog(File logFile, String name, String ip, String method){
		try{
			FileWriter LFile = new FileWriter(logFile, true);
			PrintWriter cacheLogInput = new PrintWriter(LFile);
			StringBuilder cacheLog = new StringBuilder();
			cacheLog.append(name);
			cacheLog.append(",");
			cacheLog.append(ip);
			cacheLog.append(",");
			cacheLog.append(method);
			cacheLog.append("\n");
			cacheLogInput.write(cacheLog.toString());
			cacheLogInput.close();
		}catch(IOException e){
			System.out.println("An error occurred.");
			  e.printStackTrace();
		}
	}

    public String IPselection(String[] ipList){
    //checking the number of IP addresses in the cache
	//if there is only one IP address, return the IP address
	//if there are multiple IP addresses, select one and return.
	//optional: return the IP address according to the Ping value for better performance (lower latency)
	//doesn't require this funtion, srever always returns the first ip address
		return "test";
	}

	@Override public void run(){
		BufferedReader inStream;
        PrintWriter outStream;
        try {
			//Open an input stream and an output stream for the socket
			//Read requested query from socket input stream
			//Parse input from the input stream
			//Check the requested query
			InputStream input = sSock.getInputStream();
			inStream = new BufferedReader(new InputStreamReader(input));
			OutputStream output = sSock.getOutputStream();
			outStream = new PrintWriter(output, true);
			String hostName = inStream.readLine(); 
			System.out.println("Received DNS request for: " + hostName);          
            boolean hostFound = false;	
			File cFile = null; 
			File logFile = null;
			PrintWriter logHeader = null;

			// create/open log file
			try{
				logFile = new File("dns-server-log.csv");
				if(logFile.createNewFile()){
					System.out.println("New log file created: " + logFile.getName()+"\n");
					logHeader = new PrintWriter(logFile);
					StringBuilder header = new StringBuilder();
					header.append("Host Name");
					header.append(",");
					header.append("IP");
					header.append(",");
					header.append("Method");
					header.append("\n");
					logHeader.write(header.toString());
					logHeader.close();
				}				
			}catch(IOException e){
				System.out.println("An error occurred.");
      			e.printStackTrace();
			}
			
			
			//scan local cache first			
			try{		
				cFile = new File("DNS_mapping.txt");		
				if (cFile.createNewFile()) {
					System.out.println("New cache file created: " + cFile.getName()+"\n");
				}
				Scanner scan = new Scanner(cFile);
				while(scan.hasNext()){
					String line = scan.nextLine();
					String[] nameIP = line.split(",");

					if(nameIP[0].equals(hostName)){
						System.out.println("Responsed with: " + nameIP[0]+":"+nameIP[1]+":"+"cache"+"\n");
						outStream.println(nameIP[0]+":"+nameIP[1]+":"+"cache");
						
						//write to log
						writeLog(logFile, nameIP[0], nameIP[1], "Cache");						
						hostFound = true;
					}
				}
				scan.close();
			} catch(IOException e){
				System.out.println("An error occurred.");
      			e.printStackTrace();
			}
			
			

			//use api to found ip
			if(!hostFound){
				try{
					InetAddress ipAddress = InetAddress.getByName(hostName);
					String tmp = ipAddress.toString();
					String[] apiIP = tmp.split("/");
					
					//send api ip to client, Format:<hostname>:<answer>:<how request was resolved>
					outStream.println(apiIP[0]+":"+apiIP[1]+":"+"API");
					System.out.println("Responsed with: " + apiIP[0]+":"+apiIP[1]+":"+"API"+"\n");
					
					//save new ip to cache file, Cache Format: <hostname>,<ipaddress>
					PrintWriter cache = null;
					FileWriter cacheFile = new FileWriter(cFile, true); //true tells to append data.
					cache = new PrintWriter(cacheFile);					
					cache.println(apiIP[0]+","+apiIP[1]);
					
					//log
					writeLog(logFile, apiIP[0], apiIP[1], "API");

					System.out.println("new IP added to cache"+"\n");
					cache.close();

				}catch(UnknownHostException e){
					System.out.println("Responsed with: "+hostName+":Unknown Host Name:API"+"\n");
					outStream.println(hostName+":Unknown Host Name:API");
					writeLog(logFile, hostName, "Unknown Host Name", "API");
				}
			}


			//check the DNS_mapping.txt to see if the host name exists
			//set local file cache to predetermined file.
					//create file if it doesn't exist 
					//if it does exist, read the file line by line to look for a
					//match with the query sent from the client
					//If match, use the entry in cache.
						//However, we may get multiple IP addresses in cache, so call IPselection to select one. 
			//If no lines match, query the local machine DNS lookup to get the IP resolution
			//write the response in DNS_mapping.txt
			//print response to the terminal
			//send the response back to the client
			//Close the server socket.
			
		sSock.close();
		//Close the input and output streams.


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Host not found.\n" + e);
        }
	}
}

class monitorQuit extends Thread {
	@Override public void run() {
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in)); // Get input from user.
		String st = null;
		while(true){
			try{
				st = inFromClient.readLine();
			} 
			catch (IOException e) {
			}
            if(st.equalsIgnoreCase("exit")){
                System.exit(0);
				
            }
        }
	}
}

