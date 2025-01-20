import requests
import certifi
import urllib3
from bs4 import BeautifulSoup

# Disable SSL warnings
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
        degree = element.find("span", style="color: #ff6600;").text.strip()

        # Find all <p> elements with style="text-align: center;"
        research_areas_list = element.find_all("p", style="text-align: center;")
        # Check if we have any <p> with the required style, otherwise set to N/A
        research_areas = research_areas_list[0].text.strip() if len(research_areas_list) > 0 else 'N/A'

        email_tag = element.find("a", href=True)
        email = email_tag.text.strip() if email_tag else 'N/A'

        # Find all <p> elements with style="text-align: center;" for office details
        office_list = element.find_all("p", style="text-align: center;")
        office = office_list[1].text.strip() if len(office_list) > 1 else 'N/A'

        # Append as a dictionary
        items.append({
            "name": name,
            "position": position,
            "degree": degree,
            "research_areas": research_areas,
            "email": email,
            "office": office
        })

    return items

# Example usage
if __name__ == "__main__":
    scraped_data = scrape_website()
    print(scraped_data)

    


scrape_website()