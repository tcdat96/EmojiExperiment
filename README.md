An experimental project aims to reduce the lag when starting to scroll  
The original project link: https://github.com/rockerhieu/emojicon
# Approaches
Methods I have tested in the project:
1. Original grid: the unchanged version from the original project
2. Normal grid view: a small changed from the original version, using normal TextView instead of the EmojiTextView.
3. Using multithreads: load all emojis in another thread before showing them in UI. This method produces glitches when scrolling and even not displaying correctly in some cases - definitely an unusable method. 
4. Using caches: load all emojis ahead of time by virtually drawing those emojis in an off-screen canvas. This significantly reduces the cpu processing time in the custom version of emojis, but not very efficient with the system default one.
5. Static layout: this is similar to the normal GridView method, but replacing normal TextView with StaticLayout (a more detailed explanation can be found [here](https://engineering.instagram.com/improving-comment-rendering-on-android-a77d5db3d82e)). This delivers impressive result, compared to other methods.
6. RecyclerView with GridLayout: substitute GridView with a RecyclerView, not very promising result, even lagger than the original one.
7. Vertical recyclerView: same as above.
8. TableLayout: substitute GridView with TableLayout. This is the only method satifies the expectations of the experiment. It is not perfect, of course, the major drawback is all emojis have to be added from the start, affect negatively user experience from the start and not really scalable for future development.
9. Litho: use the open-source framework from Facebook which claims to improve the scrolling performance greatly by moving the measure and layout off the main thread, flattening views, recycling primitive views. Nevertheless, the result is actually the worst in all methods, unbearable lags in every scroll gestures.
