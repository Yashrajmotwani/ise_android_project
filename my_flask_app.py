from flask import Flask, jsonify # type: ignore
# from scapeStaticFromIIT import scrape_website
from scrapeDynamicFromIIT import scrape_website

app = Flask(__name__)

# @app.route("/api/data", methods=["GET"])
# def get_data():
#     # Call the scraper
#     data = scrape_website()
#     print(data)
#     if "error" in data:
#         return jsonify(data), 500  # Return error response if scraping fails
#     return jsonify(data)  # Return scraped data as JSON


@app.route("/<college>/<department>", methods=["GET"])
def get_data(college, department):
    # Call the scraper
    data = scrape_website(college, department)
    print(data)
    if "error" in data:
        return jsonify(data), 500  # Return error response if scraping fails
    return jsonify(data)  # Return scraped data as JSON


if __name__ == "__main__":
    
    app.run(debug=True)

# api # http://127.0.0.1:5000/api/data