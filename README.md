# Read Me - TheMazeOfWazeEx3--OOP

#### This is an object oriented programmin project which his main idea is based on Graphs Algorithms. 

#### This project gives the ability to play a Game with two modes, Auto mode and Custome mode.

#### This project was wrriten by Zohar Meir & Lidor Tal during our Object Oriented Programming course at Ariel University with the guidance of Professor Boaz Ben Moshe all rights reserved to us ©.

#### This project contains an stdDraw interface which was created by Robert Sedgewick and Kevin Wayne for future use ©.

#### On this project you will find five kinds of objects : 

**[Node -](https://github.com/lidorT/Ex1---OOP/wiki/Monom)** Node represents the vertexes of the graph.

**[Edge -](https://github.com/lidorT/Ex1---OOP/wiki/Polynom)** Edge represents an edge on the graph.

**[DGraph-](https://github.com/lidorT/Ex1---OOP/wiki/Complex-Function)** represents the graph itself.

**[Robot-](https://github.com/lidorT/Ex1---OOP/wiki/Complex-Function)** represents the Robots of the game.

**[Fruit-](https://github.com/lidorT/Ex1---OOP/wiki/Complex-Function)** represents the Fruits of the game.

* In order to search a vertex or an edge or simply to pull data from the Graph withing the lowest running time complexity we used HashMap data structure in order to do some activites at the most efficient way. 

* Our idea represented at the next form:

Each DGraph has vertexes (Nodes) and Edges, Each DGraph has a HashMap field of Nodes wich there the key set as the Node Key and the values are the node_data, each DGraph also has another HashMap field which there each key is a node_data and its values are HashMaps of edge_data.

for example to get a spesific edge we will search this edge inside the Nodes HashMap and for each node we will seach on his Edges HashMap. 

There is also a class Named Graph_Algo that suppourts someo actions such as "Is Connected" - checks if a graph is Strongly Connected , "Shortest Path" return the shortest path between two vertices, "TSP" that returns the Thrill Shorted Path between input nodes.

* In general this projcet is an extendtion of [our last project](https://github.com/lidorT/OOP---Ex2) but here you can also use the graphs in order to display a game.

* In the game there are Robots that collect fruits, there are two kinds of fruits, one can be gattered from higher Vertex to a lower and the other one is the opposite (for ex : picking up a yellow fruit can only be from vertex 9 to 8 , a pink one can be picked from 8 to 9).

* When you will prees the Menu of the window you'll have the option to choose which game mode you would like to play.

example of the plot :

![ExampleOfPlot](https://i.imgur.com/MHRUO91.jpg)
