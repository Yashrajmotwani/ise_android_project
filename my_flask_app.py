from flask import Flask, jsonify # type: ignore
# from scapeStaticFromIIT import scrape_website
from scrapeDynamicFromIIT import scrape_website, scrape_website_allInfo
from pymongo import MongoClient # type: ignore

app = Flask(__name__)

# @app.route("/api/data", methods=["GET"])
# def get_data():
#     # Call the scraper
#     data = scrape_website()
#     print(data)
#     if "error" in data:
#         return jsonify(data), 500  # Return error response if scraping fails
#     return jsonify(data)  # Return scraped data as JSON


client = MongoClient("mongodb+srv://AnnavarapuSaiJagadeesh:Jagadeesh123@cluster0.zdgxc.mongodb.net/")  # Adjust the connection URI as needed
db = client["IITDB"]  # Replace with your database name
collection = db["IIT"]   # Replace with your collection name

@app.route("/")
def index():
    # print(f"Request received from: {request.remote_addr}")
    return "Hello, World!"

@app.route("/daata", methods=["GET"])
def get_all_data():
    existing_data = collection.find_one()
    if existing_data:
        print("The collection has at least one document.")
        # Fetch all documents from the collection
        all_documents = list(collection.find())
        print("All documents in the collection:", all_documents)
        for doc in all_documents:
            if "_id" in doc:
                doc["_id"] = str(doc["_id"])
        # return documents
         # Return the documents or use them as needed
        return jsonify(all_documents)
         # Return the documents or use them as needed
        return all_documents

    data = scrape_website_allInfo()
    # print(data)
    if "error" in data:
        return jsonify(data), 400  # Return error response if scraping fails
    
    collection.insert_many(data)
    return jsonify(data)    # Return scraped data as JSON


# Set up MongoDB connection



@app.route("/<college>/<department>", methods=["GET"])
def get_data(college, department):
    # Call the scraper
    print("calling \n\n\n")
    existing_data = collection.find_one()
    
    if existing_data:
        print("The collection has at least one document.")
    # Fetch all documents from the collection
        all_documents = list(collection.find())
        print("All documents in the collection:", all_documents)
        for doc in all_documents:
            if "_id" in doc:
                doc["_id"] = str(doc["_id"])
        # return documents
         # Return the documents or use them as needed
        return jsonify(all_documents)
    
    data = scrape_website(college, department)
    
    print(data)
    
    
    if "error" in data:
        return jsonify(data), 400  # Return error response if scraping fails
    
    # collection.insert_many(data)
    return jsonify(data)    # Return scraped data as JSON





if __name__ == "__main__":
    app.run(host= "0.0.0.0", port = 5000, debug=True)

# api # http://127.0.0.1:5000/api/data