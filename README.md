# Rick Wang, Zhang CSCI4211, 16/12/2021


# Description:
## Ethernet-Algorithm:
I'm using two dictionary structure as my tables. The first table "flowTable" stores the known mac addresses with the corresponding port number. And the flood table stores the unknow destination mac address with a counter. My algoritm is destination-based. When the packet arrives at the controller, the controller first check if the source address and its' port number exists in the flowtable, add them into the table if not. No further action needed here since the source mac address and input port number comes with the packet. 

Then the controller checks if the destination mac exists in the flow table, if it is, send a message that tells the switch to install a flow entry base on the existing infomation. If the port number doesn't exist, the controller check the flood table, if the unknown address exist in the flood table and its' counter is greater than 2, it means that the port of the mac address is still unkown after two new packets, so the controller sends a message to flood all the ports except the input port to obtain the port for the unknow mac address. If the counter is less than 2, increment the counter by 1, and if the unknow address doesn't exist in the flood table, add it into the table and initialize the coutner to 1. This algoritm will ensure flood doesn't happen everytime a unknown destination address shows up.

## Pseudocode:
```
if src_mac in flowTable
    if dst_mac in flowTable    
        sends msg to switch to build a new flow entry          
    else
        if dst_mac in floodTable
            if floodTable[dst_mac].counter > 2:           
                send flood msg
            else
                increment counter      
        else:
            initial counter for the new unknown mac         
else
    add port number and mac to the flow table
```
