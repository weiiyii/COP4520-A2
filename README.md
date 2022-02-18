# COP4520-A2

## Problem 1

### Installation

1. Navigate to the folder where birthdayParty.java is located
2. Compile the program by running `javac birthdayParty.java`
3. Run the program by running `java birthdayParty`

### Output

When a guest visits the labyrinth
`Guest X visited labyrinth!`

When a guest eat the cupcake
`Guest X visited ate the cupcake!`

When Guest 0 sees the cupcake's been eaten
`Guest 0 asked for another cupcake! X guests have visited the labyrinth!`

When Guest 0 counts to n-1, meaning all guests have visited the labyrinth for as least one time
`Guests have all visited the labyrinth!!`

### Proof of Correctness

There are a total of n = 10 guest in this program. Guest 0 will be the one who's responsible for counting. Here are the operations of the guests:

All guests other than Guest 0: Eat the cake only if the cake is already available at the exit && they have never had it before. Otherwise they don't do anything.
Guest 0: Add the counter if the cake is eaten. If counter == n-1, end the program and announce that all have visited the labyrinth. Otherwise call the server and request another cake at the exit.

In this case, even though a guest might visit the labyrinth multiple times, they will only eat the cake at most once, and Guest 0 will increment the counter without repetition.

### Experimental Evaluation & Efficiency

My initial thinking was to create threads with a flag `eaten`, which indicates whether or not a guest has had the cake and perform most of the operations in main thread (increment counter, eat/request cupcakes, etc). However, I realize that each thread should be running these operations independently based on different status (selected to visit the labyrinth or not) then inform the main thread.

## Problem 2

### Installation

1. Navigate to the folder where birthdayParty.java is located
2. Compile the program by running `javac birthdayParty.java`
3. Run the program by running `java birthdayParty`

### Output

When a guest visits the labyrinth
`Guest X visited labyrinth!`

When a guest eat the cupcake
`Guest X visited ate the cupcake!`

When Guest 0 sees the cupcake's been eaten
`Guest 0 asked for another cupcake!`

When Guest 0 counts to n-1, meaning all guests have visited the labyrinth for as least one time
`Guests have all visited the labyrinth!!`

### Proof of Correctness

There are a total of n = 10 guest in this program. Guest 0 will be the one who's responsible for counting. Here are the operations of the guests:

All guests other than Guest 0: Eat the cake only if the cake is already available at the exit && they have never had it before. Otherwise they don't do anything.
Guest 0: Add the counter if the cake is eaten. If counter == n-1, end the program and announce that all have visited the labyrinth. Otherwise call the server and request another cake at the exit.

In this case, each guest will only eat the cake once, and Guest 0 will increment the counter without repetition.

### Experimental Evaluation & Efficiency

My initial thinking was to create threads that would perform the same operation in run() function, which is similiar to my approach in assignment 1. However, I realize that this time each thread should be running normally instead of busy waiting for the labyrinth. Instead, main thread should randomly select a thread everytime and perform different actions based on the guest that's visiting the labyrinth.
