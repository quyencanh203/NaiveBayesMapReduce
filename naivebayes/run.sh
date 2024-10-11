hdfs dfs -mkdir /iris-input
hadoop dfs -put iris.csv /iris-input
mvn clean install -U
mvn clean
mvn package
hadoop jar target/naivebayes-1.0-SNAPSHOT.jar com.example.NaiveBayesTrainJob -D num_mappers="3" -D num_reducers="1" -D delimiter="," -D input="/iris-input/iris.csv" -D output="/outputiris" -D continousVariables="1,2,3,4" -D targetVariable="5" -D numColumns="5"
