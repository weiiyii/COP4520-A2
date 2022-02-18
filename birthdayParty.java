import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class birthdayParty {

    public Semaphore lock = new Semaphore(1);
    public boolean cupcake = true;
    public List<guestThread> threads;
    public boolean allVisited = false;


    public static void main(String[] args){

        birthdayParty program = new birthdayParty();
        program.threads = new ArrayList<>();

        for(int i=0; i<10; i++){
            guestThread th = new guestThread(program, i);
            program.threads.add(th);
            
        }

        for(guestThread th: program.threads){
            th.start();
        }

        // boolean isLocked = false;
        // int lastSelectedThread = 0;
        while(!program.allVisited){
            // while(isLocked){
            while(!program.lock.tryAcquire()){
                // System.out.println("waiting on guest " + lastSelectedThread);
                // try{

                //     Thread.sleep(1);
                // }
                // catch(Exception e){
                //     // System.out.println(e);
                //     break;
                // }
                // if(program.threads.get(lastSelectedThread).status == guestThread.guestStatus.WONDERING){
                //     // System.out.println("here");
                //     isLocked = false;
                //     // System.out.println("program lock: "+isLocked);
                // }
            }

            if(program.allVisited){
                break;
            }
            
            // System.out.println("then here, select guest");
            int rando = (int)(Math.random()*10);
            // lastSelectedThread = rando;
            // isLocked = true;
            // System.out.println("Lock aquired for guest " + lastSelectedThread);
            program.threads.get(rando).setStatus(guestThread.guestStatus.SELECTED);
            // else{
            //     // System.out.println("locked");
            // }
            // System.out.println("running");
        }


        for(guestThread th: program.threads){
            
            th.interrupt();
            
        }

        System.out.println("Guests have all visited the labyrinth!!");

    }

    public void eatCupcake(){
        this.cupcake = false;
    }

    public void requestAnotherCake(){
        this.cupcake = true;
    }

    public void markAllVisited(){
        this.allVisited = true;
    }
}

class guestThread extends Thread {

    enum guestStatus {
        WONDERING, SELECTED
    }

    public birthdayParty program;
    public boolean eaten;
    public int id;
    public guestStatus status;
    public int cnt;

    public guestThread(birthdayParty p, int id){
        this.program = p;
        this.eaten = false;
        this.id = id;
        this.status = guestStatus.WONDERING;
        this.cnt = 0;
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
            
            if(this.status == guestStatus.SELECTED){
                
                // System.out.println(id + " is up");
                // this.program.lock.acquire();
                
                if(this.id==0){
                    if(!this.program.cupcake){
                        this.cnt++;
                        this.program.requestAnotherCake();
                        System.out.println("Guest " + id + " asked for another cupcake!");
                        // System.out.println("guest 0 visiting, count: " + this.cnt);
                        if(this.cnt==9){
                            this.program.eatCupcake();
                            System.out.println("Guest " + id + " ate the cupcake!");
                            this.program.markAllVisited();
                            // TODO: Break the while loop, end all threads, announce visits
                            System.out.println("We've reached the count!");
                        }
                    }
                    else{
                        System.out.println("Guest " + id + " visited.");
                    }
                }
                else{
                    if(!this.eaten && this.program.cupcake){
                        this.eaten = true;
                        this.program.eatCupcake();
                        System.out.println("Guest " + id + " ate the cupcake!");
                    }
                    else{
                        System.out.println("Guest " + id + " visited.");
                    }
                }

                this.setStatus(guestStatus.WONDERING);
                // status = guestStatus.WONDERING;
                // System.out.println("releasing lock");
                this.program.lock.release();
                // this.program.isLocked = false;
                // this.program.isLocked.set(false);
                
            }
        }

    }
}