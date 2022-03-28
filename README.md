# comp3021lab


# HO, Yue Ho 20713304


# Code Interview for Lab<br>
First, I will go through all the tasks to check whether know the topic. In this lab, it mainly focuses on file I/O, which is a common topic for any programming language. Therefore, it is not very difficult for me.

However, the lab still has a difficult part in the 'save' function. In the first round, I just follow the instruction to implement the task, but I found that the saved object don't have any folders. Therefore, I use debug mode to track the data and find out the issue because debug mode is the most efficient to track the data and know what is wrong in our logic. Eventually, I found that I forgot to assign folders to the new object. Therefore, the new object doesn't have the folders. I just need to assign the folders to the new object and fix the issue (object.folders = this.folders).
