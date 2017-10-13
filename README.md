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
8. TableLayout: substitute GridView with TableLayout. This is the only method satifies the expectations of the experiment. It is not perfect, of course, the major drawback is all emojis have to be added initially, negatively affecting user experience from the start and not really scalable for future development.
9. Litho: use the open-source framework from Facebook which claims to improve the scrolling performance greatly by moving the measure and layout off the main thread, flattening views, recycling primitive views. Nevertheless, the result is actually the worst in all methods, unbearable lags in every scroll gestures.
# Results
To demonstrate the performance of each method, the experiment will measure correlated processing time using TraceView from Android Device Monitor. These numbers are only attempted for experimental purposes, please take these results with a grain of salt.  
Some Clarifications: 
1. **Incl** stands for **inclusive** which is the time spent on the function itself as well as the sum of the times of all functions that it calls. Wee also have the **exclusive** keyword which represents only the time spent on function itself. The exclusive time is kind of irrelevant to this context, so it is excluded from the results.
2. The experiment only focuses on the scrolling performance, other factors e.g. initial views time,... are not considered in this project.
3. The measured functions for each methods are slightly different according to their underlying structures (e.g. GridView, RecyclerView). For those ones use GridView, the chosen function is getView (except for the StaticLayout case will use both getView and TextLayoutView$onDraw). And getViewForPosition will be chosen for RecyclerView, this also includes Litho framework (yup Litho is based on RecyclerView foundation).
4. The experiment also excludes the multithreading method. Multithreading in this situation is basically a failed attempt, except for the initial stage, all parts after that are still happening in the UI thread. The results will be identical to normal GridView with no performance gain, even worse in some cases.
<table style="width:100%">
  <tr>
    <th>Method</th>
    <th>Incl CPU time</th>
    <th>Incl CPU time</th>
    <th>Incl Real time</th>
    <th>Incl Real time</th>
    <th>Calls+Recur</th>
    <th>CPU time/cal</th>
    <th>Real time/cal</th>
  </tr>
  <tr>
    <th>Original</th>
    <th>42.2%</th>
    <th>74.327</th>
    <th>3.2%</th>
    <th>105.140</th>
    <th>27.4</th>
    <th>2.723</th>
    <th>3.845</th>			
  </tr>
  <tr>
    <th>Normal GridView</th>
    <th>35.7%	</th>
    <th>55.130</th>
    <th>2.5%</th>
    <th>75.522</th>
    <th>19.2</th>
    <th>2.920</th>
    <th>3.997</th>
  </tr>
  <tr>
    <th>Caches</th>
    <th>44.3%</th>
    <th>77.473</th>
    <th>3.5%</th>
    <th>111.002</th>
    <th>27.1</th>
    <th>2.934</th>
    <th>4.202</th>
  </tr>
  <tr>			
    <th>StaticLayout</th>
    <th>10.4%</th>
    <th>11.073</th>
    <th>0.7%</th>
    <th>20.478</th>
    <th></th>
    <th></th>
    <th></th>
  </tr>
  <tr>						
    <th>RV with GridLayout</th>
    <th>38.8%</th>
    <th>126.047</th>
    <th>3.6%</th>
    <th>161.503</th>
    <th>53.0</th>
    <th>2.390</th>
    <th>3.063</th>
  </tr>
  <tr>
    <th>Vertical RV</th>
    <th>23.1%</th>
    <th>63.302</th>
    <th>2.3%</th>
    <th>89.397</th>
    <th>24.6</th>
    <th>2.638</th>
    <th>3.702</th>
  </tr>
  <tr>
    <th>Litho</th>
    <th></th>
    <th></th>
    <th></th>
    <th></th>
    <th></th>
    <th></th>
    <th></th>
  </tr>
</table>

\*This table only includes the average results, you can find more comprehensive results [here](https://vngms-my.sharepoint.com/personal/dattc2_vng_com_vn/_layouts/15/guestaccess.aspx?guestaccesstoken=yCwgDVv6pb9cdEbuDfKWkjhwk7eUwrNxvx4DdPnblH4%3d&docid=2_0a412ef26366e4bf7838e760ac139fc7a&rev=1)  
\*\*Experiment is conducted with the system default emojis (not custom emojis) in GenyMotion emulator (Google Pixel 7.1.0)
