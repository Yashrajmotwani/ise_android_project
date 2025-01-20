import requests
import certifi
import urllib3

# print(certifi.where())
from bs4 import BeautifulSoup

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

def scrape_website():
    url = "https://cse.iittp.ac.in/faculty/"  # Replace with the target URL
    response = requests.get(url, verify=False)
    
    # Check for request success
    if response.status_code != 200:
        return {"error": "Failed to fetch data", "status_code": response.status_code}

    soup = BeautifulSoup(response.text, "html.parser")
    
    items = []
    
    # Extract desired data (adjust selectors as per the site structure)
    for element in soup.find_all("div", class_="et_pb_team_member_description"):
    # Extract individual pieces of information
        name = element.find("h4", class_="et_pb_module_header").text.strip()
        position = element.find("p", class_="et_pb_member_position").text.strip()
        # degree = element.find("span", style="color: #ff6600;").text.strip()
        # research_areas = element.find_all("p", style="text-align: center;")[0].text.strip()
        # email = element.find("a", href=True).text.strip()
        # office = element.find_all("p", style="text-align: center;")[1].text.strip()

    # Append as a dictionary
        items.append({
            "name": name,
            "position": position,
            # "degree": degree,
            # "research_areas": research_areas,
            # "email": email,
            # "office": office
        })

# Print the extracted data
    # print(items)
    return items
    


scrape_website()