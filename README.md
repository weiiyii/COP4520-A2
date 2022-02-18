# COP4520-A2

## Problem 1

### Installation

1. Navigate to the folder where birthdayParty.java is located
2. Compile the program by running 
```
javac birthdayParty.java
```
3. Run the program by running 
```
java birthdayParty
```

### Output

When a guest visits the labyrinth
```
Guest X visited.
```

When a guest eat the cupcake
```
Guest X ate the cupcake!
```

When Guest 0 sees the cupcake's been eaten
```
Guest 0 asked for another cupcake! X guests have visited the labyrinth!
```

When Guest 0 counts to n-1, meaning all guests have visited the labyrinth for as least one time
```We've reached the count!  
Guests have all visited the labyrinth!!
```

### Proof of Correctness

There are a total of n = 10 guest in this program. Guest 0 will be the one who's responsible for counting. Here are the operations of the guests:

All guests other than Guest 0: Eat the cake only if the cake is already available at the exit && they have never had it before. Otherwise they don't do anything.
Guest 0: Add the counter if the cake is eaten. If counter == n-1, end the program and announce that all have visited the labyrinth. Otherwise call the server and request another cake at the exit.

In this case, even though a guest might visit the labyrinth multiple times, they will only eat the cake at most once, and Guest 0 will increment the counter without repetition.

### Experimental Evaluation & Efficiency

My initial thinking was to create threads with a flag `eaten`, which indicates whether or not a guest has had the cake and perform most of the operations in main thread (increment counter, eat/request cupcakes, etc). However, I realize that each thread should be running these operations independently based on different status (selected to visit the labyrinth or not) then inform the main thread.

## Problem 2

### Installation

1. Navigate to the folder where crystalVase.java is located
2. Compile the program by running 
```
javac crystalVase.java
```
3. Run the program by running 
```
java crystalVase
```

### Output

When a guest joins the queue
```
Guest X joined the queue
```

When a guest view the vase for the first time
```
Guest X viewed the vase for the first time!
```

When a guest view the vase again
```
Guest X viewed the vase again.
```

When a guest is done viewing and notifies the next in line
```
Guest X notified guest Y the showroom is available.
```

When all guests have viewed the vase for as least one time
```
We've reached the count!  
Guests have all viewed the crystal vase!!
```

### Proof of Correctness

Guests should choose strategy #3 as it is the one that cost the least contention. Each guest (thread) in queue will only go and visit when it's being notified while in other times they can continue enjoying the party (proceed with other tasks). Strategy #1 & #2 create a lot of traffic for the program and for each thread, and can't gaurantee a spot to be the next to view the vase (access to the lock).

This program an modification of MSC Queue Lock.

There are a total of n = 10 guest in this program with a tail referencing only the last guest in the queue. Each guest will have:

- A next pointer referencing the next guest in queue to notify.
- A counter passed by the last guest who viewed the vase.

Guests will wait until they're signaled that the showroom is available (their status changed to VIEWING by the previous guest). If this is the first time guests view the vase, they will increment the counter, otherwise not. When they are done viewing the vase, they pass the value of their current counter to the next guest in line, and signal them to go.

In this case, even though each guest can get into the queue and view the vase for multiple times, they will only increment the counter once. And each guest is responsible to notify the next guest standing in front of the queue.

### Experimental Evaluation & Efficiency

My initial approach was to utilize MSC Queue Lock. However, I realize that the program requires a counter passed through all the guest in queue. Therefore, I implemented a modification which kept the guest busy waiting after viewing the vase until the next guest in front of the queue shows up and passing the counter to the next one instead of setting the tail of the queue back to null.
