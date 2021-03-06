# Loomis-Wood-Plotter
Graphical program for displaying data in a Loomis-Wood format. The program also stores assignments and overlays the assignments onto the screen. Useful in rotational spectroscopy.

# Currently Implemented Features:
1)  View the spectra in a Loomis-Wood format by going to Display -> Create Loomis-Wood Diagram and 
    entering in the desired parameters.
2)  Navigate through the spectra by using the arrow buttons in the top right of the display
3)  Set the intensity scale (y-axis) of the display by going to Display -> Set intensity Range
4)  Right click on the diagrams to change a variety of properties of the chart. Can also left click 
    and drag on the chart to zoom in on a part of the spectra. 
5)  Under Display -> "Split the diagram into parts" you can split the diagrams into multiple pieces 
    if the spectra is too dense in a region. Saving images will react to this.
6)  Add and edit assignments by going to the Assignments tab. When editing assignments or branches,
    right clicking on the cell will bring up additional options. Note: You can copy and paste
    data straight from the tables that come up when adding or editing a branch. (Including the ability
    to paste in Excel data!)
7)  Can display assignments straight onto the Loomis-Wood plots by going to Display
    -> Toggle Assignment Overlay. Pressing this again will remove the assignments from the display.
8)  Hovering the mouse over a part of the spectra will show the points x,y values. If the assignment
    overlay is toggled on, it will show assignment information on assignmented points (eg. points
    with an upside down colored triangle)
9)  Take an image of the current Loomis-Wood display by going to Display -> Create a single
    Loomis-Wood image
10) Take images over a given range of the spectra in Loomis-Wood format by going to Display ->
    Create many Loomis-Wood images. The first field is the wavenumber value of where the images
    whill start from, the second is the wavenumber value of where the images will go to, and the
    final is the row width of the Loomis-Wood diagrams.
11) Save and Load the assignments currently in memory. This is done by going to File -> Save Assignments...
    or File -> Load Assignments... Loaded files must be ".ser" (which are created when saving assignments)
12) In the File tab, selecting "Save assignments to an Excel Peak list" will search through the peak list and add assignments that match     line positions in the file. This assumes that the wavenumbers are in the first column, intensities in the second, and that the peak     list itself is the first sheet in the workbook. This will not overwrite existing assignments listed. The program will attempt to put     the assignments in the third column, but if text is already there, then it will place it on the next empty spot for that row.

# Screenshots

![Loomis Plotter 1](https://user-images.githubusercontent.com/36011386/56087794-7976dd00-5e48-11e9-859d-6601c29f2dbd.PNG)
![Loomis Plotter 3](https://user-images.githubusercontent.com/36011386/56087795-7976dd00-5e48-11e9-8c21-3b4b3d3ae049.PNG)
![Loomis Plotter 4](https://user-images.githubusercontent.com/36011386/56087792-78de4680-5e48-11e9-876f-dd2ce2aae776.PNG)
![Loomis Plotter 5](https://user-images.githubusercontent.com/36011386/56087793-78de4680-5e48-11e9-9979-375bc4c47531.PNG)

