# DuckStories
This is a super simple app that I made to study some Android architecture and backend stuff.
The app will generate a duck, comprised of a name, picture and a short story. The user will then be
able to save, swipe to delete or edit the duck story.

Points I have focused were:

- Data persistence with Room local database and Firestore cloud database. 
- MVVM architecture, with every activity being paired with a viewModel class for data persistence
  and split responsibilities.
- Repository class for handling with db stuff, communicating with viewModels.
- recyclerView implementation, with swipe-to-delete function.
- Simple web API communication with Retrofit.


Other stuff contained:

- Internet availability live check.
- Undo function on delete action.
- Use of Glide to retrieve images from the web.
- Use of Shared Preferences and intent putExtra/getExtra for simple data handling.
- Lots of asynchronous code, using coroutines and flows.


To do:

- Login system and screen.
- Retrieve data from the cloud.


Future plans:

- Social space, where people can post and see other people's ducks.