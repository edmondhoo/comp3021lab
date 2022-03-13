# comp3021lab


# HO, Yue Ho 20713304


# Code Interview for Lab<br>
The major difficult of lab 3 is how to design and implement the function of keywords searching. Since we need to consider different cases, such as "X1 X2 OR X3" and "X1 OR X2 X3", we need to handle "AND" and "OR" case respectively.<br>

First, I will handle "OR" case. If the searching keywords contain "OR" situation, I will check the List whether contains the keywords[i-1] and  keywords[i+1]. If the List contain the keywords, I will set true and keep checking until finishing all searches.<br>

Second, I will handle "AND" case. If the List[i+1] (next) contains "OR" case, I will go to "OR" situation to check. Otherwise, I will check the List[i] whether contains the keywords. If yes, I will set true to "Result".<br>

Finally, based on the result of "result" and "resultOr", I will know that the List whether contains the keywords and put the searched object to the returning list. Therefore, we get the searching result.<br>

How to came up a solution?<br>
I will use Debug mode to check the result whether matched with my expectation. By tracking data, I will know the correctness of my logic and fix the potential bugs. Therefore, I can come up my solution.
