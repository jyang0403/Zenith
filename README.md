# TeamL-Zenith

Final notes:
- if feed/news not loading, either internet isn't working, or the API key ran out of calls. In the latter case, go to ZenithAPIHelper and change to a new key

## Group Meetings

3/27: Divided responsibilities for sprint 1, and agreed upon what should be completed by next meeting.

3/30: Met with our CA mentor, further discussed goals by next meeting and divided a few additional tasks.

4/1: Reviewed what needs to be completed in the last week

4/3: Reviewed what needs to be completed in the last week

4/5: worked together and discussed our current progress

4/6: met with CA and discussed current progress leading up to last meeting

4/8: completed our sprint 1 implementation, and worked on our presentation

4/9: recorded our presentation, submitted

4/13: discussed sprint 2 tasks and met with CA mentor again

4/17: discussed sprint 2 progress and discussed each others' work

4/20: met with CA mentor and discussed what has been completed for sprint 2, all is going well

4/21: practiced presentation and created slides

4/22: practiced presentation

4/24: practiced presentation and discussed bugs

4/27: final meeting with CA mentor

## Sprint 2: Completed Features as of April 24th ##
Feed / User Profile / Country List functionality
- Allows users to set news and country preferences and see those included in the feed
- Added refresh for feed
- Added color tags / country tag for feed
- Added "My Countries" filter button on profile
- Added error handling for when no articles can be retrieved
- Added error handling for when flags cannot be loaded

Country Profile
- Added error handling for when articles and country statistics cannot be loaded

Explore
- Added scrollable map in Explore
- Added pull-up tab
- Added error handling for when flags cannot load

Search functionality in Explore / Country List
- Search for individual countries using their name or substrings of their name
- Added error handling for no country found

General Styling
- Made views look more uniform for different devices
- Fixed styling inconsistencies
- Changed some colors

Country List / Profile styling
- Added country flag and map images using drawables / API
- Added styling for the country details / statistics page

Bug fixes
- Fixes for bugs from feed, API, country profile, and others

## Sprint 1: Completed Features as of April 9th
Basic navigation and placeholders for Sprint 2 features
- Includes custom classes, activities, layouts, etc.

API functionality
- Wikipedia API, News API, and API Ninjas country news and "This Day in History" APIs

Country history, news, and general information displayed upon clicking a country
- Accessible through the "See all" / Country List activity
- Uses APIs to populate each view

Database
- Added our functions to the DAO
- Created custom classes and the database

Country List
- Displays each country in a recyclerview, clicking results in navigating to that country's information
- Country graphics (map) and formatted information will be completed in Sprint 2
- Search and filtering will be completed in Sprint 2

Feed
- Pull to load more articles
- Currently coded to give business news from the USA
- Custom design, news categorization, randomization, and reading individual articles will be done in Sprint 2

Profile
- "User preferences" displays a list of user preferences
- "My Countries" will load list of countries saved by the user, will be completed in Sprint 2
- Functionality will be fleshed out in Sprint 2
