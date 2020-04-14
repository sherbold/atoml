[![Build Status](https://travis-ci.org/sherbold/atoml.svg?branch=master)](https://travis-ci.org/sherbold/atoml)

# Automated Testing Of Machine Learing (atoml)

atoml is a software for generating smoke and metamorphic tests for machine learning software that is currently under development. The aim of atoml is to make machine learning software more robust, i.e., ensure correct working, even if the data is extreme. 

Because this is an early research prototype, the code is partially very unrefined, e.g., the configuration of the MySQL database we use is hardcoded. 

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
-nomorph                no metamorphic tests are generated if this flag
                        is used
-nosmoke                no smoke tests are generated if this flag is used
```

A call to atoml may look like this:

```
java -jar atoml.jar -f testdata/description.yml
```

# Supported Machine Learning Frameworks

Atoml can generate tests for Weka, Scikit-Learn, and Apache Spark MLlib. Other frameworks may follow. Atoml currently only works all classification algorithms defined by those frameworks. However, support for clustering is already under development. 

# Definition of Tests

Atoml automatically generates tests from a description of the algorithm. Algorithms are described by the machine learning framework, the package and class in which they are implemented, the features they support (e.g., double, float, categorical), a set of properties that they should fulfill, and the definition of their hyperparameters. The supported features and the properties are used to decided which test cases are generated for the algorithm, i.e., which input data is suitable, which smoke tests can be executed, and which metamorphic relations should be fulfilled. The hyperparameters are used to derive tests for different combinations of hyperparameters. A grid search of the hyperparameters is due to the exponential nature of the number of allowed combinations not possible. Currently, atoml uses the default value for all except one parameter. For the remaining parameter, different values are tested. 

All the above information is defined in a YAML file. The [descriptions.yml](testdata/description.yml) file contains examples for classifier definitions as well as a description of the YAML dialect. Here is an example for the definition of tests for the DecisionTreeClassifier from scikit-learn. 

```yaml
name: SKLEARN_DecisionTreeClassifier
framework: sklearn
type: classification
package:  sklearn.tree
class: DecisionTreeClassifier
features: double
properties:
  same: score_exact
  scramble: score_exact
  reorder: score_exact
  const: score_exact
  opposite: score_exact
parameters:
  criterion:
    type: values
    values: [gini, entropy]
    default: gini
  splitter:
    type: values
    values: [best, random]
    default: best
  min_samples_split:
    type: integer
    min: 2
    max: 10
    stepsize: 2
    default: 2
  max_depth:
    type: integer
    min: 1
    max: 5
    stepsize: 1
    default: 2 
  min_samples_leaf:
    type: integer
    min: 1
    max: 13
    stepsize: 4
    default: 1
  min_weight_fraction_leaf:
    type: double
    min: 0.0
    max: 0.5
    stepsize: 0.25
    default: 0.0
  max_features:
    type: values
    values: [auto, sqrt, log2, 0.1, 0.5, 0.8, None]
    default: None
  max_leaf_nodes:
    type: integer
    min: 10
    max: 20
    stepsize: 5
    default: None
  min_impurity_decrease:
    type: double
    min: 0.0
    max: 0.4
    stepsize: 0.2
    default: 0.0
  class_weight:
    type: values
    values: [balanced, None]
    default: None
  ccp_alpha:
    type: double
    min: 0.0
    max: 0.4
    stepsize: 0.2
    default: 0.0

```

# Running tests generated with atoml

Atoml generates the tests such that they can be executed with the test runner that is already available for the programming languange with which the  tests are defined, i.e., JUnit for Weka and Spark MLlib, and the built-in Python unittest for sklearn. 

# Using the MySQL/Maria Database and the Dashboard

Atoml also provides the option to store results in a MySQL/Maria database. In this mode, the assertions of the test runners are replaced with code that stores the results in the MySQL database. The [create_mysql_schema.sql](scripts/create_mysql_schema.sql) contains the code to generate the schema for the database (notice that atoml currently requires a predefined user to write test results into the database). There is also a simple [dashboard](dashboard/main.py) that can be used to visualize the results. We recommend the creation of a virtual environment for the dashboard as follows.

```
cd dashboard
python3 -m venv .
source bin/activate
pip install -r requirements.txt
deactivate
```

The dashboard can then be started.

```
source bin/activate
python main.py
```

In case of SQL errors regarding Public Key Retrieval it might be required to add the client option "allowPublicKeyRetrieval=true" to the mysql-connector to allow the client to automatically request the public key from the server (do not use this, if the database is not a local database, since it can be used for a Man-In-The-Middle-Attack).

# License

atoml is licensed under the Apache License, Version 2.0.