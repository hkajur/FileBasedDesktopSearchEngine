/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 *
 */

# CS 345 File Based Desktop Search Engine

## Usage

Compilation

Run the following command to compile the program

```
javac fdsee.java FileInfo.java enums.java htmlParser.java stemming.java
```

Running the program

### Searching files for the readable files

Execute the following two commands to test the program
Note: Make sure to replace filename with your filename and directory with a directory

```
java fdsee searchable filename
```

```
java fdsee searchable directory
```

Running one of the previous two commands will leave you with a file called fdsee_doclist.txt file
Which has all the files that are readable in the directory or if the file is readable

### Tokenizing the readable files

```
java fdsee token filename
```

Output of this file is stored to a file named fdsee_token.txt

fdsee_token.txt is considered a dump file.

Currently, I have it so it runs the searchable each time even if you run the action token
I will have to fix that for later. But for now, it will output the string to standard output
like this: 

```
(docID, wordID, wpos, attr)    
```


```
java fdsee tokendebug filename
```

Output of this file is stored to a file named fdsee_tokendebug.txt

fdsee_tokendebug.txt is considered a dump file.

I have it set so now it accept both TEXT and HTML files

```
(docURL, wordName, wpos, attrValue)    
```

```
java fdsee stem directory
```

Output of this file is stored to a file named fdsee_lexicon.txt and fdsee_doclist.txt



```
( wordId, wordName, stopword, stemwordID)    
```


```
java fdsee stopword directory
```

Output of this file is stored to a file named fdsee_stopword.txt

fdsee_stopword.txt is considered a dump file.

```
(wordID, wordName, Stopword)    
```
