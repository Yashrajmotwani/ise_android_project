const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const app = express();
const port = 5000;

// Enable CORS for cross-origin requests from Android app
app.use(cors());
app.use(express.json());

const uri = process.env.MONGO_URI;

mongoose.connect(uri, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log('Connected to MongoDB Atlas'))
  .catch((err) => console.error('Error connecting to MongoDB Atlas:', err));

// Define the project schema
const projectSchema = new mongoose.Schema({
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

// Models
const SampleModel = mongoose.model('IITProjects', projectSchema);
const TeacherModel = mongoose.model('IITFaculty', teacherSchema);

// Define the User schema to store user's favorite projects
const userSchema = new mongoose.Schema({
  userId: String, // User ID
  favoriteProjects: [{ type: mongoose.Schema.Types.ObjectId, ref: 'IITProjects' }] // Array of references to the projects
});
const User = mongoose.model('User', userSchema);

// Define the User schema to store user's favorite teachers
const favteacherSchema = new mongoose.Schema({
  userId: String, // User ID
  favoriteTeacher: [{ type: mongoose.Schema.Types.ObjectId, ref: 'IITFaculty' }] // Array of references to the projects
});
const User_favorite = mongoose.model('Teachers', favteacherSchema);

// Define the College schema
const collegeSchema = new mongoose.Schema({
  No: String,
  Name: String,          // Name of the college, e.g., "IIT Bombay"
  Abbreviation: String,  // College abbreviation, e.g., "IITB"
  Founded: String,       // Year founded, e.g., "1958"
  'Converted as IIT': String,
  'State/UT': String,    // State where college is located, e.g., "Maharashtra"
  website: String,       // Official website, e.g., "www.iitb.ac.in"
  Faculty: String,       // Faculty count, e.g., "928"
  Students: String,      // Number of students, e.g., "15,862"
  Logo: String           // URL to the logo image, e.g., "https://example.com/logo.png"
}, {
  collection: 'iit_collegesnew' // collection name
});
const CollegeModel = mongoose.model('iit_collegesnew', collegeSchema);

// Define a search API route for projects
app.get('/search', async (req, res) => {

  const query = req.query.query;
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
   
    if (results.length === 0) {
      return res.status(404).json({ message: 'No matching records found.' });
    }
    console.log(results)
    res.json(results);
  } catch (err) {
    res.status(500).json({ message: 'Error fetching data from MongoDB', error: err });
  }
});


// Define a search API route for faculty
app.get('/fsearch', async (req, res) => {
  
  const query = req.query.query;
  
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

    if (results.length === 0) {
      return res.status(404).json({ message: 'No matching records found.' });
    }

    res.json(results);
  } catch (err) {
    res.status(500).json({ message: 'Error fetching data from MongoDB', error: err });
  }
});

// Save project to user's favorites
app.post('/saveProject/:userId', async (req, res) => {
  const { userId} = req.params;
  const projectData = req.body;

  try {
    // Find the project by ID
    const project = await SampleModel.findById(projectData._id);
    if (!project) {
      
      return res.status(404).json({ message: 'Project not found' });
    }

    // Find the user
    let user = await User.findOne({ userId });

    if (!user) {
      // Create a new user if the user does not exist
      user = new User({ userId, favoriteProjects: [projectData._id] });
      await user.save();
    } else {
      // Add the project to the user's favorites
      if (!user.favoriteProjects.includes(projectData._id)) {
        user.favoriteProjects.push(projectData._id);
        await user.save();
      }
    }

    res.status(200).json({ message: 'Project saved successfully' });
  } catch (err) {
    res.status(500).json({ message: 'Error saving project', error: err });
  }
});

// Remove project from user's favorites
app.delete('/removeProject/:userId/:projectId', async (req, res) => {
  const { userId, projectId} = req.params;

  try {
    // Find the user
    const user = await User.findOne({ userId });
    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    // Remove the project from the user's favorites
    const index = user.favoriteProjects.indexOf(projectId);
    if (index === -1) {
      return res.status(404).json({ message: 'Project not found in favorites' });
    }

    user.favoriteProjects.splice(index, 1); // Remove the project from the array
    await user.save();

    res.status(200).json({ message: 'Project removed from favorites' });
  } catch (err) {
    res.status(500).json({ message: 'Error removing project', error: err });
  }
});

// Get user's favorite projects
app.get('/getFavoriteProjects/:userId', async (req, res) => {
  const { userId } = req.params;

  try {
    // Find the user and populate the favorite projects with full details
    const user = await User.findOne({ userId }).populate('favoriteProjects');
    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    // Send the populated favorite projects
    res.status(200).json(user.favoriteProjects);
  } catch (err) {
    res.status(500).json({ message: 'Error fetching favorite projects', error: err });
  }
});

// Save project to user's favorites
app.post('/saveTeacher/:userId', async (req, res) => {
  const { userId } = req.params;
  const teacherData = req.body;

  try {
    // Find the project by ID
    const project = await TeacherModel.findById(teacherData._id);
    if (!project) {
      
      return res.status(404).json({ message: 'Teacher Details not found' });
    }

    // Find the user
    let user = await User_favorite.findOne({ userId });

    if (!user) {
      // Create a new user if the user does not exist
      user = new User_favorite({ userId, favoriteTeacher: [teacherData._id] });
      await user.save();
    } else {
      // Add the teacher to the user's favorites
      if (!user.favoriteTeacher.includes(teacherData._id)) {
        user.favoriteTeacher.push(teacherData._id);
        await user.save();
      }
    }

    res.status(200).json({ message: 'Teacher saved successfully' });
  } catch (err) {
    res.status(500).json({ message: 'Error saving teacher details', error: err });
  }
});

// Remove project from user's favorites
app.delete('/removeTeacher/:userId/:teacherId', async (req, res) => {
  const { userId, teacherId } = req.params;

  try {
    // Find the user
    const user = await User_favorite.findOne({ userId });
    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    // Remove the project from the user's favorites
    const index = user.favoriteTeacher.indexOf(teacherId);
    if (index === -1) {
      return res.status(404).json({ message: 'Teacher not found in favorites' });
    }

    user.favoriteTeacher.splice(index, 1); // Remove the teacher from the array
    await user.save();

    res.status(200).json({ message: 'Teacher removed from favorites' });
  } catch (err) {
    res.status(500).json({ message: 'Error removing teacher', error: err });
  }
});

// Get user's favorite projects
app.get('/getFavoriteTeacher/:userId', async (req, res) => {
  const { userId } = req.params;

  try {
    // Find the user and populate the favorite projects with full details
    const user = await User_favorite.findOne({ userId }).populate('favoriteTeacher');
    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    // Send the populated favorite projects
    res.status(200).json(user.favoriteTeacher);
  } catch (err) {
    res.status(500).json({ message: 'Error fetching favorite teachers', error: err });
  }
});

// Define a search API route for colleges
app.get('/colleges', async (req, res) => {
  try {
    const colleges = await CollegeModel.find();  // Fetch all colleges
    console.log(colleges)

    res.json(colleges);
  } catch (err) {
    res.status(500).json({ message: 'Error fetching colleges', error: err });
  }
});

// Start the server
app.listen(port, '0.0.0.0', () => {
  console.log(`Server running on http://localhost:${port}`);
});
