from scrapeDynamicFromIIT import scrape_website, scrape_website_allInfo
from pymongo import MongoClient # type: ignore

# Set up MongoDB connection
client = MongoClient("mongodb+srv://AnnavarapuSaiJagadeesh:Jagadeesh123@cluster0.zdgxc.mongodb.net/")  # Adjust the connection URI as needed
db = client["IITDB"]  # Replace with your database name
collection = db["IIT"]   # Replace with your collection name

print("calling \n\n\n")
existing_data = collection.find_one()
print("called \n\n\n")

if existing_data:
    print("The collection has at least one document.")