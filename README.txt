This is the code for my implementation of the Popular Movies project for the Udacity Android nanodegree program.

The project is currently on the second phase (popular, most rated and favorite lists, movie details, reviews and videos).

The project has been implemented following the arquitecture recommended by Google for new Android projects, based on the use of LiveDatas and data repositories.

Apart from the requirements of the exercise, I've added the following features in order to practice more:

- Implementation of the scroll up to refresh UI pattern with the SwipeRefreshLayout element.
- Implementation of the scroll down to load more items UI pattern by the use of paged lists and data sources.
- Additional info (for instance, movie name and rating on the lists, genres and production countries on the movie details).
- Support for various languages, included the request to REST API (currently English and Spanish).
- Movie cast and crew for the movie info.
- Access to web page of the movie if existing.
- Save at database movie details, cast and crew.

The interface with the database has been implemented using retrofit library, with gson converter library for transforming JSON response to application objects. The classes representing these objects have been created with the help of http://www.jsonschema2pojo.org/ online application.

The entities used by the application have been created by my own and are translated from the Rest API entities.

For database persistence, Room component has been used, using the same global entities as Database entities (in order not to repeat code).

UI data filling relies mainly on Data Binding component.

Some of the main libraries used are the following:
- LiveData
- PagedLists
- Room
- DataBinding
- Dagger2
- Retrofit
- Gson

The application has been tested with the following devices, with no problems:
- Virtualized Google Nexus 4 with API 19.
- Real Xiaomi Mi5s with API 26.
- Virtualized Google Pixel XL with API 28.

My intention has been for main classes to have at least an initial comment if not autoexplanatory, in order to guide the review of the code. Additional comments have been added alon both java and XML code in order to explain decissions made.

Lint analysis has been passed, with no errors. A few warnings have been supressed. Apart from this, redundant and unused parameters warnings have been deliberately left without resolve them because as the application is still in development, I think it's better to remove unnecesary elements once it's sure they are unnecessary. There are some spelling warnings which have also been omitted on purpose.

