# TransferGrid
This library uses reflection to create a JavaFX GridPane with all annoted variables aswell as fields to edit them.

## Table of content

- [Why](#Reasoning)
- [Goals](#Goals)
- [Current Status](#Current-Status)
- [HowTo](#HowTo)
  - [Usage](#Usage)

## Why

After quite some work on my [Graphical Programming Interface](https://github.com/FancyJavaStuff/GPI) and using quite a bit of [GSON](https://github.com/google/gson), I decided to create a way of generating a GridPane for objects. This lead me to this library, which was primarly developed on the GPI repo and has now been separated into a separate one, for everyone to use.

This was heavily inspired by [GSON](https://github.com/google/gson) and it was used as reference for quite some stuff, so thank you [@Google](https://github.com/google)!

Many thanks also go out to [I-Al-Istannen](https://github.com/I-Al-Istannen) who did some initial splitting up of v1.0.0 into something more structured.

The whole library was improved and gives the user now more customizability, whilst also increasing its functional scope. Objects in Objects? No problem, we can include them! Enums? No problem, TransferGrid generates a comboBox which gives the user all enum options!

Check out the [wiki](https://github.com/FancyJavaStuff/TransferGrid/wiki) or the [Example](https://github.com/FancyJavaStuff/TransferGrid/tree/master/src/main/java/Example) to find out more.
## Goals

Currently there are no set goals. Ofcourse, bug fixes are part of it, but I will continue work on the [GPI](https://github.com/FancyJavaStuff/GPI) before thinking about extending TransferGrid. If you have any suggestion/idea, feel free to share it!

## Current Status

~~Currently Version 1.0 is released and free to use. If you find any issues, please report them. Feel free to also give any recommendations!~~ 

**Version 1.7.0 has been released!**
This version now has 2 Interfaces that can be used for notifications of whenever a reflective action happens.

This feature also added a GitHub Page to this repository that is contains the whole javaDoc of this library. The github Page can be found [here](https://fancyjavastuff.github.io/TransferGrid/javaDoc/index.html)

This repo is also avaliable as a Maven dependency and can be found over at [jitpack.io](https://jitpack.io/#FancyJavaStuff/TransferGrid/v1.7.0).

## HowTo

A how to can be found in the [wiki](https://github.com/FancyJavaStuff/TransferGrid/wiki)

## Usage

This repo contains a folder [Example](https://github.com/FancyJavaStuff/TransferGrid/tree/master/src/main/java/Example)

The main class contains the following setup for the usage of the TransferGrid:
```
  ExampleObject exampleObject = new ExampleObject();
  ReflectorGrid reflectorGrid = new ReflectorGrid();
  reflectorGrid.setFieldNamingStrategy(DefaultFieldNamingStrategy.SPLIT_TO_CAPITALIZED_WORDS);
  reflectorGrid.setLabelDisplayOrder(LabelDisplayOrder.SIDE_BY_SIDE);
```

This gives us the following Result:

![Result](https://github.com/FancyJavaStuff/TransferGrid/blob/master/pictures/GeneratedGrid_v1_5_2_wiki_1.png)

As you can see, you get a barebones grid. You can then set it up to your liking with spacing and all. An additional benefit of this library: setting the paddings etc. once is enough, however many times you generate a grid, they will stay the same!
