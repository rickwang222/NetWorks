# Rick Wang, Zhang CSCI4211, 10/20/2021

Java,DNSServer.java,,DNSServer.class

Use "javac DNSServer.java" to complie.

Use "java DNSServer" to run the program. 

The Domain Name System (DNS) provides a vital service to the modern Internet, translating
human-friendly and easily readable hostnames to the IP addresses routing protocols used to
direct connections and packets. Many protocols such as HTTP, SMTP, and FTP use this the
DNS system.
The server is listening to port 8990, for every client that connect through this port, the server create a thread for tha client.  The the client thread, the server first check for the "DNS_mapping.txt" cache file, the srever will create one if this file doesn't exit yet. Then the server performs same opeartion with the "dns-server-log.csv" log file. When a client send a host name, the server first check the cache file, and if there is no match, the server will use "InetAddress.getByName" function to get the ip address and then save it to the cache. All the communications are saved in the log file at the end of the thread.

