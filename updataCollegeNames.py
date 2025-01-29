from pymongo import MongoClient # type: ignore

# Connect to MongoDB
client = MongoClient("mongodb+srv://iseiittpdev2025:79exGUcy50QqTN15@cluster0.x64u9.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
db = client["IITDB"]  # database name
# collection = db["IITFaculty"]  # collection name
collection = db["IITProjects"]  # collection name

# Mapping of old values to new values
updates = {
    "iittp": "IIT Tirupati",
    "iitg": "IIT Gandhi Nagar",
    "iitk": "IIT Kanpur"
}

# looging through the mapping and update documents
for old_value, new_value in updates.items():
    result = collection.update_many(
        {"college": old_value},  # get documents with the old value
        {"$set": {"college": new_value}}  # setting new value
    )
    print(f"Updated {result.modified_count} documents where college was '{old_value}'.")

print("Update complete.")
