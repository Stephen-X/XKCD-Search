# XKCD Comics Search Server
**Author:** Stephen Tse \<***@cmu.edu\>  
**Project Version:** 1.0.0

A simple search engine for [XKCD](https://xkcd.com/), a webcomic of romance, sarcasm, math, and language.

To test run the server, import the project into [NetBeans](https://netbeans.org/features/index.html) then press `F6`.


## Some Notes

1. Since XKCD does not provide public API for content access, this project utilized the [web scraping](https://en.wikipedia.org/wiki/Web_scraping) technique to extract content from XKCD. Last time I checked, [it is legal to do so on XKCD](https://xkcd.com/license.html), as long as the project is not for commercial use. However, any future update from Randall to the XKCD front-end might break the server's functionalities.

2. The search is not case-sensitive, but may produce unexpected results, as the server only checks for matching substrings in comic titles. For example, a search for "Rain" will also include comics such as "Brain Upload".

3. To prevent the server from constantly parsing and extracting content from XKCD for each web request, the comic list extraction process happens only once when the server boots up. Therefore, the search server will not reflect the latest updates of XKCD until site admin reboots it. However, the server will try to reconnect and refresh its state once if connection to XKCD failed in a web request.


## Screenshots

<img src="screenshots/1.1 Welcome Page - Success.png" width="400" alt="Screenshot1">

<img src="screenshots/2.2.2 Results Page - Multiple Titles - Third Request (Different Cases).png" width="400" alt="Screenshot2">

<img src="screenshots/1.2 Welcome Page - Failed.png" width="400" alt="Screenshot3">
