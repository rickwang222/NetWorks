public class B {
    int nextSeq;

    public B(){
        //  initialization of the state of B
        // this.seq
        // ...
        nextSeq = 0;

    }
    public void B_input(simulator sim,packet p){
        //  process the packet recieved from the layer 3
        // verify checksum
        // send ACK
        if(nextSeq == p.seqnum){
            if(p.checksum == p.get_checksum()){                  //send ack if seq and checksum are both correct
                packet.send_ack(sim, 'B', nextSeq+1);
                sim.to_layer_five('B',p.payload);
                nextSeq++;
            }
            else{
                packet.send_ack(sim, 'B', nextSeq * -1);        //send nack if checksum are corrupted
                
            }            
        }
        else{
            //System.out.println("wrong seq, resend last ack");
            packet.send_ack(sim, 'B', nextSeq);
        }
    }
    public void B_output(simulator sim){

    }
    public void B_handle_timer(simulator sim){

    }
}
