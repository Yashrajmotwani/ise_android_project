# ISE ANDROID STUDIO PROJECT - IIT TIRUPATI CSE DEPT
This github repository contains the code for the Industrial Software Android project, the APK, code that has been used to run the backend, and the scripts for web scraping of IIT Faculty data and Open Projects across multiple IITS. It also contains the code that has been used to scrap the IIT colleges across the World.

## Use Case
We are looking to create an app for all IITs, where the students can view the teachers' profiles across IITs based on their area of research, name or college. We are also showing the open positions across IITs that the users can apply for. Both would be done by getting the data using web scraping. Additionally, we would also display the Colleges on a Map and pop notifications when the User if within a given radius of an IIT. This data too has been acquired by Web Scraping from Wikipedia.

## Features
1. Splash Screen with Logo
2. Login screen to Sign In from google email
3. Search for Open Positions using colleges, name of projects, etc.
4. Search for Faculty using Area of Research, colleges, department, name, etc.
5. On clicking the cardview, it opens a dialog view to display additional details of the positions or the faculty.
6. For faculty, if the Phone number or email is provided, on clicking that, it opens the Phone app with phone number set and the email app with the receiver email set.
7. Save, Remove Open Positions and Faculty Profiles from the Dialog view
8. Can see Saved data in separate Favorites sections using the Floating Action Button (FAB)
9. Implemented BottomSheet Dialog linked to the FAB to show other features
10. View different colleges using the Map API
11. View your current location and receive notification if within a radius of an IIT
12. View all the IITs, their details, and on clicking the cardview, can go to their respective websites
13. Switching between fragments with highlights and colors using Bottom Navigation View (kept at the top)
14. Sign Out feature

## Working
To run the app properly, we need to start the render so that the link - BASE_URL in retrofit client will work and allow the connection from the database. The login details are shared in a file in the assignment submission in Classroom.  
If we do not want to use render, replace the BASE_URL (RetrofitClient.kt) to the IP in your system,
It will look something like this:
```
val BASE_URL = "http://a.b.c.d:5000/" // ipv4_address - a.b.c.d
```

To get your IP Address:
- Run in the Command prompt terminal:
```
ipconfing
```
- Pick the IPv4 address
  
Additionally, for all the app features to work make sure the following are enabled:
1. Internet - for connection to database
2. Location - for the Maps/Geofence features and sending notifications based on that  

Also, it should be ensured that app is allowed access to the location when the View Maps feature is used, which might require you to go back and restart that feature.
    
## Database Details  

We are using the MongoDB Atlas Database to store the information of Faculty, Open Projects, Colleges along with the users favorites - teacher profiles and open projects. This allows the user for easier access to preferred Profiles and Projects in Future.  

Database contains the following Faculty and Open Projects:    
| IITs             | Project Positions | Faculty Details               |
|------------------|-------------------|-------------------------------|
| IIT Tirupati     |  &nbsp; ✅        | All Departments ✅            |
| IIT Gandhi Nagar |  &nbsp; ✅        | All Departments ✅            |
| IIT Hyderabad    |  &nbsp; ✅        | CSE Department ✅             |
| IIT Bombay       |  &nbsp; ✅        | ❌                             |
| IIT Kanpur       |  &nbsp; ✅        | ❌                             |
| Total Records    | 800+              | 400+                          |

  
Database contains the following Colleges:    
| Collection Name    | Documents | Logical Data Size | Avg Document Size | Storage Size | Indexes | Index Size | Avg Index Size |
|--------------------|-----------|-------------------|-------------------|--------------|---------|------------|----------------|
| IITFaculty         | 401       | 168.68KB          | 431B              | 112KB        | 1       | 44KB       | 44KB           |
| IITProjects        | 808       | 255.83KB          | 325B              | 156KB        | 1       | 64KB       | 64KB           |
| iit_collegesnew    | 25        | 8.51KB            | 349B              | 36KB         | 1       | 20KB       | 20KB           |
| teachers           | 2         | 354B              | 177B              | 36KB         | 1       | 36KB       | 36KB           |
| users              | 4         | 470B              | 118B              | 36KB         | 1       | 36KB       | 36KB           |


## Individual Work Done

### 1. Yashraj Motwani (CS24M104):
- Maintaining GitHub repo
- Integration of features built by team members into the app
- Built app design
- Wrote the backend code for DB Connection
- Connected Database and backend to get, store, and delete data in separate Fragments
  - Displayed Faculty, Open Projects and College data from the Database
  - Saving the Faculty and Open Projects
  - Removing any of the Saved Faculty details or Open projects
  - Added dialog view on click of the cards to show additional information, including images
- Added phone calls and email sending on click from the app and handled Phone, Email null cases


### 2. Tejas Meshram (CS24M108):
- Developed Sign-In and Sign-Out feature leveraging Google Firebase for authentication.
- Designed and implemented an intuitive Login Page for user access.
- Created a Splash Screen to enhance user experience.
- Integrated Google Maps API to visualize all IIT colleges on an interactive map.
- Implemented Geofencing API to display real-time user location and proximity to IITs.
- Added Notification Alerts
  - users receive notifications when they enter a predefined range of an IIT.
- Web-scraped comprehensive data of all IIT colleges and efficiently stored it in MongoDB Atlas using Compass to access in-app.


### 3. Sai Jagadeesh (CS24M101):
- Web scrapped IIT project positions and faculty details  
- Created and hosted APIs online for scrapping live data  
- Stored the scrapped data in Mongo Atlas database to access in-app  
- Deployed the backend of the app
