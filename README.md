# MorningSignOff

This is the Android Studio project files for the Morning Signout application. (Name to be fixed later).

## Structure of Android Application
The current structure of the application consists of 3 pages.

1.  The front page will contain categories of articles and the latest publications 
2.  The categories page will contain article titles, images, and descriptions 
3.  Article pages will simply consist of title, image, article, and author.

Settings, subscription, etc. will be considered at a later time.

## Status
#### Front page 
* List of categories 
* Buttons with animation 
* Split screen between picture and list (for latest news/article categories)

#### Networking
* Parsed categories pages into list of Article objects consisting of title, description, and link [Example of parsed page]
(http://morningsignout.com/category/research/)

#### Team member goals
* Fire3galaxy - Working on creating activity for click event from front page to category

## Current goals
* Create activity to be created on clicking category buttons.
* Integrate network calls to new activity to load actual article data.
* Integrate network calls to headline fragment in front page to show images/articles
