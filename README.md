# tweetr
A personal project for a small scale Twitter like service.

# Scope
1. Starting with 10K users
2. Each user sends averagely 10 messages a day

# Tech Stack
1. Spring Boot
2. Java
3. REST Api's
4. Apache Kafka

To install and start Kafka locally (https://gist.github.com/sieyip/3cff306e35fc74a81f886178900dfb1f)

The current implementation is Proof of Concept to conceptualize the idea of a small Twitter like service. 
It does not connect to a Database, neither embeds an in-memory database. Currently, data is stored using in-memory collections. 

# Flow
1. Users could be added using "SetupSeedData" test class. In reality, a user could be added after being authenticated via the appropriate Identity Provider. 
2. Any user can post a Tweet OR ReTweet
3. While posting a Tweet, the user could mention another user in the text using "@<username>". 
4. An Activity Feed is created for every user mentioned AND/OR following that tweet. 
5. Any user in the system could fetch their Feed using a REST APi
6. Once a user fetches their feed, all activity is current erased. This is considering with the idea that clients would be polling for Feed in a certain duration, and they should only get what is NEW and not yet retrieved. 
The Activity fetching could be further enhaced to consider more parameters that a calling client would need to send such as "last timestamp of calling the Feed REST Api" or "top most id of the tweet/reTweet of the last Feed's response". 


# APi's
1. GET /rest/users  --> Retrieves all users (Pagination is PENDING)

2. GET /rest/user/{id} --> Retrieves a User

3. POST /rest/user/{userId}/follow/user/{followId} --> Creates a relation where {userId} is following {followId}

4. DELETE /rest/user/{userId}/follow/user/{followId} --> Removes the relation where {userId} is following {followId}

5. POST /rest/tweet --> Creates a Tweet. Requires "user_id" (UUID of the user creating the tweet) in the Header. Ideally, the Header has the authentication token and an application could figure out the user from the token. But since there is no authentication mechanism included here, thus involving the "user_id" in the HEADER manually. 

Request Body required {"text" : }

6. GET /rest/tweet/{id} --> Retrieves a Tweet, which includes 
    i. its Id
   ii. User who created this tweet.
  iii. Text
   iv. TimeStamp
    v. List of valid people mentioned in this tweet using "@<username>"
   vi. Comments (Not implemented the feature to add a comment).

7. POST /rest/reTweet -> Creates a ReTweet. Requires "user_id" (UUID of the user creating the tweet) in the Header
  Request Body required {"tweetId" : }
  
8. GET /rest/user/{userId}/tweets  --> Retrieves all Tweets and ReTweets by the given user in descending order of creation. i.e most recently created is first. (Pagination is PENDING)

9. GET /rest/user/{id}/feed -> Retrieves most recent Tweets and ReTweets of users that the given {userId} follows. Also includes Tweets where the given {userId} was mentioned. (Pagination is PENDING)


# Design
Option 1 (Brute Force).

Given the fact that there are atleast 10K uses, tweeting 10 messages averagely a day, lets do some maths
Daily  ~100,000 messages will be sent in total
Monthly ~2,000,000 messags (considering only weekdays i.e 5 working days * 4 weeks)
Yearly ~24,000,000 messages

DB -> ORACLE
There would be three tables

USERS (stores a User)
USER_FOLLOWS (userid , follow id)
TWEETS (tweetid, tweet details, createdUserId)

The Tweets table would have 24Million rows in a year. In about 4 years it would reach 1Billion rows and counting. 
The above is keeping in mind no extra users were added and the average number of messages per day were the same. However, realistically the number of users increases and the number of messages sent would increase. 

Activity Feed would have to be a query to the Tweets table joined with the users followed by the given "user" asking for the feed. Realistically, any user averagely follows about 200 people. Joins would be possible, but would become expensive eventually when more messages are sent per day and number of users following increases. 

The above clearly shows that DB Scaling is going to be required in 3-4 years. Oracle has the ability to scale vertically and partition data as well. However, it would be prefered to scale horizontally.

##The above design would not scale well in future years. 


Option 2 (Slightly optimized). 

Instead of Activity Feed being a query to the Tweets table, what if for every Tweet posted, the system could ASYNCHRONOUSLY append an activity on every interested individual user's activity stream data store? 

Keeping ORACLE AS the DB, the schema would be
USERS (stores a User)
USER_FOLLOWS (userid , follow id)
TWEETS (tweetid, tweet details, createdUserId)
USER_ACTIVITY (user_id, tweet_id)

Thus, every User's feed would only need to query the USER_ACTIVITY table and get the data faster. There would be processing time involved per tweet posted as a DOWNSIDE.

The above could work well until the number of users dont increase. Even when the TWEETS table grows to 1Billion rows, we would only fetch Tweets from the table with a given TWEET_ID (which would be indexed). Practially, it would be slow to create indexes with a Billion rows in the table, however, this could still work for 10K users. 

But when the number of Users increase to about 1M, Horizontal Scaling is must and ORACLE would NOT be the right choice.


Option 3 (Preferred).
Activity Feed is processed ASYNCHRONOUSLY by the system whenever a tweet is posted, and appends the tweet on every interested individual user's activity stream store

Scope
Users ~1M+
Daily messages ~10messages per user per day --> ~10M+ messages per day
Monthly messages --> ~200M messages
Yearly --> ~2.4Billion messages

Datastore 
1. Cassandra -- to store all the Tweets and Activty Stream per user.
2. Graph DB -- to store all users and their relations
3. Kakfa -- cluster of queues and multi-threaded consumers to examine Tweets under different topics to
    a. Insert Activity of follower / mentioned in the tweet (achieved by current POC)
    b. Recommendation analysis
    c. Analytics 
    
# Running this app
1. Start Zookeeper and Kafka cluster
2. Start the application --> mvn spring-boot:run
3. Run "SetupSeedData" test class to add users
4. Run "AddTweets" test class to add Tweets

# Enhancements
1. The current POC could be futher enhanced to connect to the above DB's.
2. Also would be great to try out an alternative solution of Activity (which is querying the tweet datastore for recent tweets instead of processing/preparing the feed upfront, as done currently). 
3. Concurrent collections / concurrency handling. 
4. API for Comments
5. Recommendations Algorithm
6. Implement the pending Pagination for certain APi's. 
