# Automated Testing Of Machine Learing (atoml)

atoml is a software for generating smoke and metamorphic tests for machine learning software that is currently under development. The aim of atoml is to make machine learning software more robust, i.e., ensure correct working, even if the data is extreme. 

# Usage

atoml is a command line tool that can eb used with the following options. 

- -f,--file <arg>: input file that contains classifiers
- -i,--iterations <arg>: number of iterations used by smoke tester (default: 1)
-  -l,--mllib <arg>: ML library for which tests are generated (default: weka, allowed: weka, scikit)
- -m,--nfeat <arg>: number of features for each generated test set (default: 10)
-  -n,--ninst <arg>: number of instances generated for each test set (default: 100)
-  -r,--resourcepath <arg>: path where generated test data is stored (default: src/test/resources/)
-  -t,--testpath <arg>: path where generated test cases are stored (default: src/test/java/)

Examples for files that define the classifiers for which tests are generated can be found in the [testdata](testdata) folder. Examples for command line calls for the test generation can be found below.

# Examples

JUnit generation for Weka:

```
java -jar atoml.jar -f testdata/wekaclassifiers.txt -i 5 --testpath testres/weka/src/test/java/ --resourcepath testres/weka/src/test/resources/
```

unittest generation for scikit-learn:

```
java -jar atoml.jar -m 2 -n 10 -l scikit -f testdata/scikitclassifiers.txt -i 5 --testpath testres/scikittests/ --resourcepath testres/scikittests/
```

JUnit generation for Spark MLlib

```
java -jar atoml.jar -l spark -f testdata/sparkclassifiers.txt -i 5 --testpath testres/spark/src/test/java/ --resourcepath testres/spark/src/test/resources/
```

# License

CrossPare is licensed under the Apache License, Version 2.0.