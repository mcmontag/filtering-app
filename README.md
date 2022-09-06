# NR Take-Home Exercise - Filtering App
#### Matt Montag

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [How To Run the App](#how-to-run-the-app)
- [Design Decisions](#design-decisions)
- [Opportunities For Improvement](#opportunities-for-improvement)

## Overview
This application does two simple things: sorts and searches a "customers" dataset. Users are able to navigate to either the sort page or the search page within a browser, where they can interact with inputs to alter their view of the data.

### Search
When navigating to the search page (http://localhost:3000/customers/search), the user is presented with an unfiltered view of (some of) the data, along with a text box.  Whenever (with a slight buffer) the user enters a letter, the frontend sends the current value of the textbox to the backend, which queries the database for customers whose first OR last name contains the input as a substring, ignoring case.  In addition to updating the view of the data, each letter entered (or deleted) by the user is reflected within the URL as a query parameter. These query parameters are evaluated (and inserted into the search bar) on page load, allowing URLs to be copy/pasted, shared and re-visited.

### Sort
When navigating to the sort page, the user is presented with a similar table.  However, they now have a dropdown available, rather than a textbox.  The dropdown contains six possible sorting opt+ions, one for each (useful -- i.e., non-`id`) field and sort direction:
1. First Name, Ascending
2. First Name, Descending
3. Last Name, Ascending
4. Last Name, Descending
5. Company Name, Ascending
6. Company Name, Descending

As with the text input on the search page, changing the input immediately updates the table (by calling the backend, which handles the sorting) and persists the selection within the URL.

### Tech Stack
The tech stack is as follows:
- Backend: Java 11 (Spring Boot)
- Database: In-memory H2, managed entirely via Spring Boot auto-configuration 
- Frontend: Angular 14

## How to run the app
I endured a few headaches so you wouldn't have to -- at the expense of a few unfinished requirements, if I'm being honest.  Here's what you need to do in order to run the app:

1. If you don't have a minimually up-to-date Docker installation (or, failing that, a standalone Docker Compose installation), install/update it.
2. Clone this repo (`git clone https://github.com/mcmontag/filtering-app`)
3. Navigate to the root directory and run `docker compose up`

The application will build and start -- the backend first, followed by the frontend.  Once the frontend is up, navigate to either http://localhost:3000/customers/search or http://localhost:3000/customers/sort -- there is no landing page, which is a disappointing oversight on my part, but each page has a button to take you to the other for convenience.

The main challenge here, and the reason I went the Docker Compose route, was npm.  I'm working off of a relatively old, underpowered backup laptop that I hadn't been keeping updated, so it took a few hours to get things in working order.

## Design Decisions
As with any project, there are always a hundred ways to approach the problem, and about half of them are entirely valid. This led to some lost time, as I went a little too deep into the weeds on some features, and not remotely deep enough on others.  At a high-level, I went with a fairly run-of-the-mill architecture.

### Assumptions about requirements
- The frontend enforces a limit of 100 results at a time.  This was techincally intended to be just the default limit, with a maximum of 500 being allowed by including a limit parameter on search/sort pages.  That ended up being overkill (and wasn't a requirement), so I scrapped it, but it felt like reasonable functionality to add at the time. I considered enforcing the limit on the backend, but generally speaking I prefer to make backing services more flexible and allow clients to be pickier.

- I originally planned on having both the sort and search features exist on the same page, with either a toggle between them, or support for composable queries.  After looking over the requirements again, the "unfiltered" verbiage in the Sort description convinced me to give the features separate pages.

- I treated this like a strictly internal application with zero security requirements whatsoever.

### The Database
I admittedly took some shortcuts here, as it felt fairly pointless to fret too much about the database when Spring's JPA framework makes it a complete non-issue. H2 was particularly attractive as an option because a) it has a small footprint, and I was working with well under 8GB of RAM, b) it requires ZERO configuration when used with the Spring Boot Autoconfiguration package, and c) it's an in-memory database, providing minimal latency -- I felt this was an important benefit given the "snappy" feel that the search UI needed.

### The Backend
I spent far too much time on the SortingService, and slipped a few tricks up its sleeve that will never, ever be used (null handling, for example).  One consequence of that was that I didn't end up getting full test coverage (not a trap I typically fall into, which was surprising); I ended up testing it only through CustomerController. 

Speaking of CustomerController, it sits at the center of a very simple design:
- CustomerController, a bean/component, handles incoming requests from the frontend/whoever
  - I ended up having to scrap a handful of configuration from this class (i.e. all of the routes and default behaviors) due to some...technical difficulties (getting it to be present and accessible when running the .jar). 
- Method name-based auto-implementation is used for name queries -- it felt like cheating, so I limited it to that usage.
- CustomerController delegates sorting to a SortingService<Customer>, which is a prototype-scoped bean/component that requires similar setup to a HashMap, in that entries (sortable field names coupled with a getter or comparator allowing that field to be sorted) are "registered" one at a time.

### The Frontend
Fairly typical Angular app -- components, services, routing, data binding etc. There are two main components, SearchComponent and SortComponent, each of which relies upon CustomerService to handle data retrieval from the backend. It's been a little while since I've worked on a fresh, new Angular app, so there were some small hiccups (e.g. using [value] instead of [NgValue] led to some headaches while trying to get two-way binding working with both the Sort dropdown and the URI/route).
  
## Opportunities for Improvement
By far the biggest disappointment here is that I have a whopping 0% test coverage for the frontend. I ran out of time, and something had to give.  Apart from that, there were a couple of funny little quirks and bugs -- for example, single quotes are showing as double quotes in the table.  
