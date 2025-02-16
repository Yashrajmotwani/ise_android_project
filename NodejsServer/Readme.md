# ise_android_project
Adding the Code for the Node Js Backend to get the render link for the backend connection with the app.

### If using locally, steps to run this:
1. Change the uri to the connection string of the database.
2. Run the below command:
```
nodemon server.js
```
3. Ensure the BASE_URL in RetrofitClient.kt is correct:
- For physical devices, it should be:
> val BASE_URL = "http://a.b.c.d:5000/"

### If running using render:
Ensure the BASE_URL in RetrofitClient.kt for Render is:
> val BASE_URL = "https://web-service-name.onrender.com/"

This will run the server.js that you have connected to the render Web Service