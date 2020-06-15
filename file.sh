#!/bin/bash


javac ServerCode.java
gnome-terminal --command 'java ServerCode'


javac ClientCode.java


for i in {1..5};
do
gnome-terminal --command 'java ClientCode';
done;
