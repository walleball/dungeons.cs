Adding new monsters:
--------------------
Add new monster class (Troll.jsl)
Copy code from relevant existing monster (search and replace)
GameEngine.jsl:
	Increment kNumMonsterTypes
	Update getRandomMonster
	
MonsterCounter.jsl
	Update toString and getContentString

