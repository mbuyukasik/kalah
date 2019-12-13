  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.2.RELEASE)
 
KALAH GAME REST APPLICATION
 
 @author 		: Mehmet Buyukasik
 @Date			: 13.12.2019
 @Reason		: Backbase assignment
 @Description	: This is a spring boot application designed for kalah game. Number of stones is configurable. (Default 6)


CONFIGURATION
-------------
application.properties file keeps configuration information. 

There are two application parameters:
GAME_CODE_LENGTH : This is the length of the game code which is used in game url. 
                   This way, game url is UNPREDICTABLE AND SECURE
NUMBER_OF_STONES : 6 stone kalah is requested, but applied a generic solution. 
                   This paremeter is used to change number of stones (For new games).

Other parameters are all about datasource definition

LOGGING
-------
Log configuration can be found under resources\logback.xml

DATABASE
----------
Application is tested with both MySql and H2 databases. 
Configuration for both dbms can be found in application.properties. 
Structure of the database is kept in schema.sql. As can be seen GAME, PLAYER and MOVE tables exist.
MOVE table is a transaction table to trace players' moves.
If records of a specific game are ordered with seq field, all moves of players can be traced

EXCEPTION HANDLING
------------------
OperationResultException is the custom exception class which is thrown from the application 
for any invalid operation or an unexpected value. Information about the error is kept in OperationResult property.
KalahExceptionHandler is the main exception handler and is used to return readable error details for specific problems.

DOCUMENTATION
-------------
* All classes and methods have information in java doc format
* Swagger is used for REST api documentation. 
  After the application is up http://localhost:8080/swagger-ui.html can be used to see api method details

TEST
------

Test cases for most rules of Kalah game are implemented in TestKalahController class.
Each method includes information of its purpose. 
Another random game playing method is implemented (testRandomGame).
It creates a game and sends valid move requests until the game ends.
Outputs of a full valid game in MOVE table helped me a lot in finding defects.


