public class circular_buffer {
    int read;
    int write;
    int max;
    int count;
    packet [] buffer;

    public circular_buffer(int max){
        this.max=max;
        this.buffer= new packet[max];
        this.read=0;
        this.write=0;
    }

    public void push(packet pkt){
        if(this.count==max){
            return;
        }
        else{
            this.buffer[write]=pkt;
        }
        this.write=(this.write+1)%this.max;
        this.count++;
    }

    public void pop(){
        if(this.count==0){
            return;
        }
        this.read=(this.read+1)%this.max;
        this.count--;
    }

    public packet[] read_all(){
        packet[] pkt_array= new packet[count];
        int r=this.read;
        for (int i=0;i<count;i++){
            pkt_array[i]= this.buffer[r];
            r = (r+1)%count;
        }
        return pkt_array;
    }

    public boolean isfull(){
        if(this.count==this.max){
            return true;
        }
        return false;
    }
}
