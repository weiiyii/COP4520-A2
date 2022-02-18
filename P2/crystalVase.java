import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class crystalVase {

    public Semaphore qLock = new Semaphore(1);
    public Semaphore roomLock = new Semaphore(1);
    public List<guestThread> threads;
    public boolean allVisited = false;
    public guestThread tail = null;


    public static void main(String[] args){

        crystalVase program = new crystalVase();
        program.threads = new ArrayList<>();

        for(int i=0; i<10; i++){
            guestThread th = new guestThread(program, i);
            program.threads.add(th);
            
        }

        for(guestThread th: program.threads){
            th.start();
        }

        boolean firstIn = false;
        // int lastSelectedThread = 0;
        while(!program.allVisited){
            try{

                Thread.sleep(1);
            }
            catch(Exception e){
                // System.out.println(e);
                break;
            }

            // kick off: get the first guest in line
            if(!firstIn && program.tail != null){
                program.tail.setStatus(guestThread.guestStatus.VIEWING);
                firstIn = true;
            }
        }


        for(guestThread th: program.threads){
            
            th.interrupt();
            
        }

        System.out.println("Guests have all viewed the crystal vase!!");

    }

    public void markAllVisited(){
        this.allVisited = true;
    }
}

class guestThread extends Thread {

    enum guestStatus {
        WONDERING, WAITING, VIEWING
    }

    public crystalVase program;
    public int id;
    public boolean visited;
    public guestStatus status;
    public int cnt;
    public guestThread next;
    

    public guestThread(crystalVase p, int id){
        this.program = p;
        this.id = id;
        this.visited = false;
        this.status = guestStatus.WONDERING;
        this.cnt = 0;
        this.next = null;
    }

    public void setStatus(guestStatus s){
        this.status = s;
        // System.out.println("updated status: " + id + ": " + status); 
    }

    @Override
    public void run(){
        // System.out.println("thread " + id + " starting");
        while(true){
            try{

                Thread.sleep(1);
            }
            catch(Exception e){
                // System.out.println(e);
                break;
            }
            // System.out.println("guest " + id + " running");
            
            if(this.status == guestStatus.WONDERING){
                
                if(Math.random() < 0.1){
                    try{
                        program.qLock.acquire();
                        guestThread pred = program.tail;
                        if(pred != null){
                            pred.next = this;
                        }
                        program.tail = this;
                        this.setStatus(guestStatus.WAITING);
                        System.out.println("Guest " + id + " joined the queue.");
                        program.qLock.release();
                    }
                    catch(Exception e){
                        // System.out.println(e);
                        // Thread.currentThread().interrupt();
                        break;
                    }
                }
                
            }
            else if(this.status == guestStatus.VIEWING){
                try{
                    
                    program.roomLock.acquire();
                    if(!this.visited){
                        this.visited = true;
                        this.cnt++;
                        System.out.println("Guest " + id + " viewed the vase for the first time!");
                    }
                    else{
                        System.out.println("Guest " + id + " viewed the vase again.");
                    }
                    this.setStatus(guestStatus.WONDERING);
                    program.roomLock.release();

                    if(this.cnt==10){
                        System.out.println("We've reached the count!");
                        program.markAllVisited();
                        break;
                    }
                    else{
                        while(this.next == null){
                            // busy wait
                        }
                        System.out.println("Guest " + id + " notified guest " + this.next.id + " the showroom is available.");
                        this.next.cnt = this.cnt;
                        this.next.setStatus(guestStatus.VIEWING);
                    }
                }
                catch(Exception e){
                    // System.out.println(e);
                    break;
                }
            }
        }

    }
}

