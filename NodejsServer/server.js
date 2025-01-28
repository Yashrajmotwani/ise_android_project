const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const app = express();
const port = 5000;

// Enable CORS for cross-origin requests from Android app
app.use(cors());
app.use(express.json());

const uri = 'mongodb+srv://iseiittpdev2025:79exGUcy50QqTN15@cluster0.x64u9.mongodb.net/IITDB?retryWrites=true&w=majority&appName=Cluster0';

mongoose.connect(uri, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log('Connected to MongoDB Atlas'))
  .catch((err) => console.error('Error connecting to MongoDB Atlas:', err));

// Define a schema for your database (use one of your collections as an example)
const sampleSchema = new mongoose.Schema({
  name_of_post: String, // "Research Associate (RA-I)"
  discipline: String, // "Materials Engineering"
  posting_date: String, // "24.01.2025"
  last_date: String, // "31.01.2025"
  pi_name: String, // "Prof. Emila Panda"
  advertisement_link: String, // "https://drive.google.com/file/d/1S7v5pxBlz_3punyBT7qjSpOkbTPO339d/view…"
  status: String, // "Open"
  college: String, // "iitg"
  department: String, // "project_positions"
}, {
  collection: 'IITProjects' // Explicitly specify the collection name
});

const teacherSchema = new mongoose.Schema({
    name: String, // "Dr. Sridhar Chimalakonda"
    position: String, // "Associate Professor"
    qualification: String, // "Ph.D - International Institute of Information Technology Hyderabad, In…"
    areas_of_interest: String, // "Software Engineering, Computing for Education"
    phone: String, // "0877 250 3211"
    email: String, // "ch@iittp.ac.in"
    image_link: String, // "https://files.iittp.ac.in/images/demo/iit/chimalakonda.jpg"
    college: String, // "iittp"
    department: String, // "cse"
}, {
  collection: 'IITFaculty' // Explicitly specify the collection name
});

const SampleModel = mongoose.model('IITProjects', sampleSchema); 
const TeacherModel = mongoose.model('IITFaculty', teacherSchema); 


// Define a search API route
app.get('/search', async (req, res) => {
  const query = req.query.query; // Search query parameter
  console.log(query)
  try {

    const results = await SampleModel.find({
      $or: [
        { name_of_post: new RegExp(query, 'i') },
        { discipline: new RegExp(query, 'i') },
        { last_date: new RegExp(query, 'i') },
        { pi_name: new RegExp(query, 'i') },
        { status: new RegExp(query, 'i') },
        { college: new RegExp(query, 'i') },
        { department: new RegExp(query, 'i') }
      ]
    });
    
    console.log(results);

    if (results.length === 0) {
      return res.status(404).json({ message: 'No matching records found.' });
    }

    res.json(results);
  } catch (err) {
    console.error('Error fetching data:', err); // Log the error if any
    res.status(500).json({ message: 'Error fetching data from MongoDB', error: err });
  }
});

app.get('/fsearch', async (req, res) => {
  const query = req.query.query; // Search query parameter
  console.log(query)
  try {

    const results = await TeacherModel.find({
      $or: [
        { areas_of_interest: new RegExp(query, 'i') },
        { name: new RegExp(query, 'i') },
        { position: new RegExp(query, 'i') },
        { qualification: new RegExp(query, 'i') },
        { college: new RegExp(query, 'i') },
        { department: new RegExp(query, 'i') }
      ]
    });
    
    console.log(results);

    if (results.length === 0) {
      return res.status(404).json({ message: 'No matching records found.' });
    }

    res.json(results);
  } catch (err) {
    console.error('Error fetching data:', err); // Log the error if any
    res.status(500).json({ message: 'Error fetching data from MongoDB', error: err });
  }
});


// Start the server
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});
