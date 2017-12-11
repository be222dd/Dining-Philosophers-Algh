import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Philosopher implements Runnable {
	
	private int id;
	private boolean isHungry=true;
	volatile boolean stopYet = false;
	public boolean debug=false;
	
	private final ChopStick leftChopStick;
	private final ChopStick rightChopStick;
	
	private Random randomGenerator = new Random();
	
	private int numberOfEatingTurns = 0;
	private int numberOfThinkingTurns = 0;
	private int numberOfHungryTurns = 0;

	private double thinkingTime = 0;
	private double eatingTime = 0;
	private double hungryTime = 0;
	
	public Philosopher(int id, ChopStick leftChopStick, ChopStick rightChopStick, int seed) {
		this.id = id;
		this.leftChopStick = leftChopStick;
		this.rightChopStick = rightChopStick;
		
		/*
		 * set the seed for this philosopher. To differentiate the seed from the other philosophers, we add the philosopher id to the seed.
		 * the seed makes sure that the random numbers are the same every time the application is executed
		 * the random number is not the same between multiple calls within the same program execution 
		 */
		
		randomGenerator.setSeed(id+seed);
		
	}
	public int getId() {
		return id;
	}

	public double getAverageThinkingTime() {
		/* TODO
		 * Return the average thinking time
		 * Add comprehensive comments to explain your implementation
		 */
		return thinkingTime/numberOfThinkingTurns;
	}

	public double getAverageEatingTime() {
		/* TODO
		 * Return the average eating time
		 * Add comprehensive comments to explain your implementation
		 */
		
		return eatingTime/numberOfEatingTurns;
	}

	public double getAverageHungryTime() {
		/* TODO
		 * Return the average hungry time
		 * Add comprehensive comments to explain your implementation
		 */
		return hungryTime/numberOfHungryTurns;
	}
	
	public int getNumberOfThinkingTurns() {
		return numberOfThinkingTurns;
	}
	
	public int getNumberOfEatingTurns() {
		return numberOfEatingTurns;
	}
	
	public int getNumberOfHungryTurns() {
		return numberOfHungryTurns;
	}

	public double getTotalThinkingTime() {
		return thinkingTime;
	}

	public double getTotalEatingTime() {
		return eatingTime;
	}

	public double getTotalHungryTime() {
		return hungryTime;
	}
	
	public void think() throws InterruptedException{
		
		int thinkTurnTime = randomGenerator.nextInt(1000)+0;
		
		if(debug)
			System.out.println("PHILOSOPHER_"+this.getId()+" is THINKING turns:"+numberOfThinkingTurns+" for "+thinkTurnTime+"ms");

		Thread.sleep(thinkTurnTime);
		thinkingTime+= thinkTurnTime;
		numberOfThinkingTurns++;
	}
	
	public void eat() throws InterruptedException{
		
		int eatTurnTime = randomGenerator.nextInt(1000)+0;
		
		if(debug)
			System.out.println("PHILOSOPHER_"+this.getId()+" is EATING turns:"+numberOfEatingTurns+" for "+eatTurnTime+"ms");
		Thread.sleep(eatTurnTime);
		eatingTime+=eatTurnTime;
		numberOfEatingTurns++;
	}
	@Override
    public void run() {
		
		//this implementation prevents deadlocks by using reEntarant locks 
		//and releasing the locks after some time to give others the opportunity
		

		try {
	        while (!stopYet) {
	          // THINKs for a while
	          think();
	          //gets HUNGRY
	          isHungry=true;
	          long startHungryTime = System.currentTimeMillis();
	          if(debug)
	        	  System.out.println("PHILOSOPHER_"+this.getId()+" is HUNGRY");

	          //Trys to get both chopsticks to eat.
	          while(isHungry){
	        	  //try to get left chopstick for some time 
	        	  if (leftChopStick.pickUp(this, "left")) {
	        		  //try to get right chocstick for some time 
	  	            if (rightChopStick.pickUp(this, "right")) {
	  	            	//both chopsticks aquired.Stop hungry time 
	  	            	long endHungryTime = System.currentTimeMillis();
		  	            long hungryTurnTime=endHungryTime-startHungryTime;
		  	            hungryTime+=hungryTurnTime;
		  	            numberOfHungryTurns++;
		  	            //eat
		  	            eat();
	  	              
		  	            isHungry=false;
		  	            // Finished.
		  	            rightChopStick.putDown(this, "right");
	  	            }
	  	            // Finished.
	  	            leftChopStick.putDown(this, "left");
	  	          }
	        	}
	          
	          
	        }
	      } catch (Exception e) {
	        // Catch the exception outside the loop.
	        e.printStackTrace();
	      }
    }

   

    

   
	
	
	
	
	
}
