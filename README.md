#AI_Sandbox_Command
By Todd Davies
Licenced under Creative Commons Attribution 3.0 Unported (http://creativecommons.org/licenses/by/3.0/)
You can give me attribution by including a link to my website (tjmd.co.uk) and email address (todd434@gmail.com)

##How does this commander work
I wanted to split Micro and Macro management into distinct areas within the code. 
I made a Unit class and a UnitInterface that provides a way for high level commands such as 'move here' to be handled by the Unit in a low level way (such as having a Squad class override Unit and move a set of Bots each protecting each others back)
I also made a Strategy class and StrategyInterface. These are designed to be overriden and provide the base code to issue high level macromanagement commands to multiple units. For example, HunterKillerStrategy sends units to random parts of the map to hunt for enemy.

All the commander really has to do is to assign his bots to Units, and then assign the units to strategies.