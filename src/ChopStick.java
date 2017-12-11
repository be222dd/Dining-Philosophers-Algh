import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {
    
    Lock lock = new ReentrantLock(true);
    public boolean debug=false;
    
    
    private final int id;

    public ChopStick(int id) {
      this.id = id;
    }
    //Using reentrantLock philosopher tries to lock for some time otherwise returns false
    public boolean pickUp(Philosopher phil, String chopstick) throws InterruptedException {
      if (lock.tryLock(300, TimeUnit.MILLISECONDS)) {
    	if(debug)
    		System.out.println("PHILOSOPHER_"+phil.getId() + " picked up " + chopstick + " " + this);
        
        return true;
      }
      return false;
    }

    //Unlocks the chopstick to make it available again
    public void putDown(Philosopher phil, String name) {
      lock.unlock();
      if(debug)
    	  System.out.println("PHILOSOPHER_"+phil.getId() + " put down " + name + " " + this);
    }

    @Override
    public String toString() {
      return "Chopstick-" + id;
    }
  }