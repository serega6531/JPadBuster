# Introduction
This is a simple Java implementation of the Padding Oracle attack ([article](https://robertheaton.com/2013/07/29/padding-oracle-attack/)).

You can use this repository to create your own implementation of oracle (thing that decides whether padding on your message is correct or not).
## Instructions
To create an oracle you have to implement Oracle interface with `boolean tryDecrypt(byte[] encrypted)` method, and return true when the padding is correct (server returned no error), or false otherwise.
You can find simple example of it in `ExampleOracle.java`

Then just create an instance of a `PadBuster` with your block size (usually it's 16 for AES):
```
PadBuster padBuster = new PadBuster(16, oracle);  //when you don't know the IV (first block will not be decrypted)
PadBuster padBuster = new PadBuster(16, oracle, "INSERT_IV_HERE".getBytes(StandardCharsets.UTF_8))  // when you know the IV
```

Then just call `padBuster.attack(enc)`, where `enc` is your array of bytes with ciphertext, and go get a cup of coffee.
Program will show all decrypted bytes step by step, so you can keep track of the progress. Method will return an array with decrypted bytes, already cleared of padding.
If you didn't specify IV, first block of it will be filled with '-'.

## Example
You can find an example in `Main.java`. Here is the program output:
```
iv = IdQxrmE5k9gUNT1B
key = FRyesumfSzfmxf3q
ygp3FyoWUk2B0hMnrcJH7J9nsgtIdsg1Op5ivwoNVOIQFDYAYpTAvTT
---------------------------------------------------------------	
--------------------------------------------------------------		
-------------------------------------------------------------			
------------------------------------------------------------				
-----------------------------------------------------------					
----------------------------------------------------------						
---------------------------------------------------------							
--------------------------------------------------------								
-------------------------------------------------------									
------------------------------------------------------T									
-----------------------------------------------------TT									
----------------------------------------------------vTT									
---------------------------------------------------AvTT									
--------------------------------------------------TAvTT									
-------------------------------------------------pTAvTT									
------------------------------------------------YpTAvTT									
-----------------------------------------------A
----------------------------------------------YA
---------------------------------------------DYA
--------------------------------------------FDYA
-------------------------------------------QFDYA
------------------------------------------IQFDYA
-----------------------------------------OIQFDYA
----------------------------------------VOIQFDYA
---------------------------------------NVOIQFDYA
--------------------------------------oNVOIQFDYA
-------------------------------------woNVOIQFDYA
------------------------------------vwoNVOIQFDYA
-----------------------------------ivwoNVOIQFDYA
----------------------------------5ivwoNVOIQFDYA
---------------------------------p5ivwoNVOIQFDYA
--------------------------------Op5ivwoNVOIQFDYA
-------------------------------1
------------------------------g1
-----------------------------sg1
----------------------------dsg1
---------------------------Idsg1
--------------------------tIdsg1
-------------------------gtIdsg1
------------------------sgtIdsg1
-----------------------nsgtIdsg1
----------------------9nsgtIdsg1
---------------------J9nsgtIdsg1
--------------------7J9nsgtIdsg1
-------------------H7J9nsgtIdsg1
------------------JH7J9nsgtIdsg1
-----------------cJH7J9nsgtIdsg1
----------------rcJH7J9nsgtIdsg1
---------------n
--------------Mn
-------------hMn
------------0hMn
-----------B0hMn
----------2B0hMn
---------k2B0hMn
--------Uk2B0hMn
-------WUk2B0hMn
------oWUk2B0hMn
-----yoWUk2B0hMn
----FyoWUk2B0hMn
---3FyoWUk2B0hMn
--p3FyoWUk2B0hMn
-gp3FyoWUk2B0hMn
ygp3FyoWUk2B0hMn
ygp3FyoWUk2B0hMnrcJH7J9nsgtIdsg1Op5ivwoNVOIQFDYAYpTAvTT
MATCHES
```

Feel free to use this program in any situation you want.