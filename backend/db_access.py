import MySQLdb

def Connect():
    return MySQLdb.connect(host="localhost", user="root", passwd="34bfd806ff", db="TrendingFlicks")
	
def seenFlick(imdbId, userId):
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""INSERT INTO `seenFlicks` (imdbId, userId) VALUES (%s, %s);""", (imdbId, userId));
	Db.commit()
	Db.close()
	return True

def unSeenFlick(imdbId, userId):
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""DELETE FROM `seenFlicks` WHERE imdbId=%s AND userId=%s;""", (imdbId, userId));
	Db.commit()
	Db.close()
	return True	

def isSeenFlick(imdbId, userId):
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""SELECT userId FROM `seenFlicks` WHERE imdbId=%s AND userId=%s""", (imdbId, userId));
	for Current in Cursor.fetchall():
		if Current[0] == userId:
			Db.close()
			return True
	Db.close()
	return False
	
def getSeenFlicks(userId):
	list = []
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""SELECT imdbId FROM `seenFlicks` WHERE userId='"""+userId+"""'""");
	for Current in Cursor.fetchall():
		list.append(Current[0])
	Db.close()
	return list
	
	
def likeFlick(imdbId, userId):
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""INSERT INTO `likeFlicks` (imdbId, userId) VALUES (%s, %s);""", (imdbId, userId));
	Db.commit()
	Db.close()
	return True

def unlikeFlick(imdbId, userId):
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""DELETE FROM `likeFlicks` WHERE imdbId=%s AND userId=%s;""", (imdbId, userId));
	Db.commit()
	Db.close()
	return True	

def isLikedFlick(imdbId, userId):
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""SELECT userId FROM `likeFlicks` WHERE imdbId=%s AND userId=%s""", (imdbId, userId));
	for Current in Cursor.fetchall():
		if Current[0] == userId:
			Db.close()
			return True
	Db.close()
	return False
	
def getLikedFlicks(userId):
	list = []
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""SELECT imdbId FROM `likeFlicks` WHERE userId='"""+userId+"""'""");
	for Current in Cursor.fetchall():
		list.append(Current[0])
	Db.close()
	return list
	
	
def clearTopFlicks():
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""DELETE FROM `topFlicks`;""");
	Db.commit()
	Db.close()
	return True	

def addTopFlicks(type, imdbId, imdbRating):
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""INSERT INTO `topFlicks` (flickType, imdbId, rating) VALUES (%s, %s, %s);""", (type, imdbId, imdbRating));
	Db.commit()
	Db.close()
	return True
	
def getTopFlicks():
	list = []
	Db = Connect()
	Cursor = Db.cursor()
	Cursor.execute("""SELECT flickType, imdbId, rating FROM `topFlicks`;""");
	for Current in Cursor.fetchall():
		list.append({'flickType':Current[0],'imdbId':Current[1], 'imdbRating':Current[2],})
	Db.close()
	return list