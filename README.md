# CS 345 File Based Desktop Search Engine

## Usage

Compilation

```
javac FDSEE.java FileInfo.java enums.java htmlParser.java
```

Running the program

### Searching files for the readable files

```
java FDSEE searchable filename
```

```
java FDSEE searchable directory
```

Running the previous command will leave you with a file called FDSEE_searchable.txt file
Which has all the files that are readable in the directory or if the file is readable

### Tokenizing the readable files

```
java FDSEE token filename
```

Currently, I have it so it runs the searchable each time even if you run the action token
I will have to fix that for later. But for now, it will output the string to standard output
like this: 

```
(docID, wordID, wpos, attr)    
```

Later on, I need to fix this so it will output to a file instead of standard output
