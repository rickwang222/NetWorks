public class A {
    String state;
    int seq;
    int estimated_rtt;
    packet lastpacket;
    public A(){
        // initialization of the states of A
        // seq
        // estimated rtt
        // ....
        state = "WAIT_LAYER5";
        estimated_rtt = 30;
        seq = 0;
    }
    public void A_input(simulator sim, packet p){
        //  recive data from the other side
        // process the ACK, NACK from B
        if(state.equals("WAIT_ACK")){
            if(p.acknum == seq + 1){                            //checking ack
                sim.envlist.remove_timer();
                seq ++;                                          //increment seq for next msg
                state = "WAIT_LAYER5";    
            }
            //negative ack for nack
            else if(p.acknum == (seq * -1)){
                sim.envlist.remove_timer();
                //System.out.println("Corrupted data, resend packet:" + lastpacket.seqnum);
                sim.to_layer_three('A', lastpacket);            //nack, resend lastpacket
                sim.envlist.start_timer('A', estimated_rtt);
            }
            else{
                 System.out.println("Wrong ACK, Do nothing");
            }
        }
        else{
            System.out.println("Not, expecting input");
        }

    }
    public void A_output(simulator sim, msg m){
        //  called from layer 5, pass the data to the other side
        if(state.equals("WAIT_LAYER5")){
            lastpacket = new packet(seq,0,m);
            sim.to_layer_three('A', lastpacket);
            state = "WAIT_ACK";                                         
            sim.envlist.start_timer('A', estimated_rtt);
            //System.out.println("data sent");
        }
        else{
            System.out.println("waiting ack, cannot process msg");
        }
    }
    public void A_handle_timer(simulator sim){
        //  handler for time interrupt
        // resend the packet as needed
        //System.out.println("timeout resend packet:" + lastpacket.seqnum);
        sim.to_layer_three('A', lastpacket);
        sim.envlist.start_timer('A', estimated_rtt);        //resend packet
    }
}
