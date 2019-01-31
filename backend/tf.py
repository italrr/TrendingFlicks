import json
import time
import datetime
import tornado.ioloop
import tornado.web
import db_access
import unirest
import random
from threading import Thread

def scrambled(orig):
    dest = orig[:]
    random.shuffle(dest)
    return dest

class Handler(tornado.web.RequestHandler):
    def get(self):
		reply = {'status': 'SUCCESS'}
		userId = self.get_argument('userId')
		method = self.get_argument('method')
		
		# Top Flicks
		if method == 'topFlicks':
			topFlicks = db_access.getTopFlicks()
			reply['list'] = topFlicks
						
		# Like Flick
		if method == 'isLikedFlick':
			imdbId = self.get_argument('imdbId')
			liked = db_access.isLikedFlick(imdbId, userId)
			reply['liked'] = liked
			
		elif method == 'likeFlick':
			imdbId = self.get_argument('imdbId')
			db_access.likeFlick(imdbId, userId)
			reply['liked'] = True
			
		elif method == 'unlikeFlick':
			imdbId = self.get_argument('imdbId')
			db_access.unlikeFlick(imdbId, userId)
			
		elif method == 'getLikedFlicks':
			reply['list'] = db_access.getLikedFlicks(userId)
			
		# Seen Flick
		if method == 'isSeenFlick':
			imdbId = self.get_argument('imdbId')
			seen = db_access.isSeenFlick(imdbId, userId)
			reply['seen'] = seen
			
		elif method == 'seenFlick':
			imdbId = self.get_argument('imdbId')
			db_access.seenFlick(imdbId, userId)
			reply['seen'] = True
			
		elif method == 'unSeenFlick':
			imdbId = self.get_argument('imdbId')
			db_access.unSeenFlick(imdbId, userId)
			
		elif method == 'getSeenFlicks':
			reply['list'] = db_access.getSeenFlicks(userId)			
						
			
		self.write(json.dumps(reply))

class Manager(Thread):
	apiKey = '1fa9b154d5251a1793489aca3999febc'
	urlMovieDb = 'http://api.themoviedb.org/3/'
	urlOMDb = 'http://www.omdbapi.com/'
	lastMakePopularYearly = 0
	def run(self):
		while(True):
			if int(time.time()) - self.lastMakePopularYearly > 86400:
				date = datetime.datetime.now()
				finalList = []
				
				# Fetch Top 20 TV
				tv = unirest.get(self.urlMovieDb+'discover/tv', headers={ "Accept": "application/json" }, params={'api_key':self.apiKey,'year':date.year})
				if tv.code == 200:
					tvList = json.loads(tv.raw_body)
					for current in tvList['results']:
						time.sleep(0.5)
						result = unirest.get(self.urlMovieDb+'tv/'+str(current['id'])+'/external_ids', headers={ "Accept": "application/json" }, params={'api_key':self.apiKey}).body
						imdbId = result['imdb_id']
						# get rate from OMDb
						resultRate = ''
						try:
							resultRate = unirest.get(self.urlOMDb, headers={ "Accept": "application/json" }, params={'i':imdbId}).body
						except:
							pass
						rating = resultRate['imdbRating']
						finalList.append({'type':'tv', 'imdbId':imdbId, 'rating':rating})
					
				# Fetch Top 20 Movies
				movies = unirest.get(self.urlMovieDb+'discover/movie', headers={ "Accept": "application/json" }, params={'api_key':self.apiKey,'year':date.year})
				if movies.code == 200:
					movieList = json.loads(movies.raw_body)
					for current in movieList['results']:
						time.sleep(0.5)
						result = unirest.get(self.urlMovieDb+'movie/'+str(current['id']), headers={ "Accept": "application/json" }, params={'api_key':self.apiKey}).body
						imdbId = result['imdb_id']
						# get rate from OMDb
						resultRate = ''
						try:
							resultRate = unirest.get(self.urlOMDb, headers={ "Accept": "application/json" }, params={'i':imdbId}).body
						except:
							pass
						rating = resultRate['imdbRating']						
						finalList.append({'type':'movie', 'imdbId':imdbId, 'rating':rating})
				
				scrambled(finalList)
				scrambled(finalList)
				scrambled(finalList)
				db_access.clearTopFlicks()
				
				for flick in finalList:
					db_access.addTopFlicks(flick['type'], flick['imdbId'], flick['rating'])
				
				self.lastMakePopularYearly = int(time.time())
				
				print 'Made Top Flicks List'
				
			time.sleep(0.15)

		
if __name__ == "__main__":
	managerThread = Manager()
	managerThread.start()
	app = tornado.web.Application([(r"/", Handler)])
	app.listen(8185)
	tornado.ioloop.IOLoop.current().start()