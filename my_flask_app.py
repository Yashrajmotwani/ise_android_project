from flask import Flask, jsonify
from scapeFromIIT import scrape_website

app = Flask(__name__)

@app.route("/api/data", methods=["GET"])
def get_data():
    # Call the scraper
    data = scrape_website()
    print(data)
    if "error" in data:
        return jsonify(data), 500  # Return error response if scraping fails
    return jsonify(data)  # Return scraped data as JSON

if __name__ == "__main__":
    
    app.run(debug=True)

# api # http://127.0.0.1:5000/api/data