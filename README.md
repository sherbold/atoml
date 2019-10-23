# Automated Testing Of Machine Learing (atoml)

atoml is a software for generating smoke and metamorphic tests for machine learning software that is currently under development. The aim of atoml is to make machine learning software more robust, i.e., ensure correct working, even if the data is extreme. 

# Usage

atoml is a command line tool that can be used with the following options. 
```
-f,--file <arg>         input file in yaml format (mandatory)
-i,--iterations <arg>   number of iterations used by smoke tester
                        (default: 1)
-m,--features <arg>     number of features for each generated test set
                        (default: 10)
-mysql                  the results are stored in a local MySQL database
                        if this flag is used
-n,--instances <arg>    number of instances generated for each test set
                        (default: 100)
-nomorph                no metamorphic testa are generated if this flag
                        is used
-nosmoke                no smoke tests are generated if this flag is used
```

A call to atoml may look like this:
```
java -jar atoml.jar -f testdata/description.yml
```

# Definition of Tests

Tests for atoml are defined using a YAML file. Within the YAML file the algorithms under test are specified. atoml can generate tests for different learning frameworks and types of algorithms at the same time. The [descriptions.yml](testdata/description.yml) file contains examples for classifier definition as well as a description of the YAML dialect. 

# License

atoml is licensed under the Apache License, Version 2.0.