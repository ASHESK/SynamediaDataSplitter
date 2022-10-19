# SynamediaDataSplitter <a href="https://github.com/ASHESK/SynamediaDataSplitter"><img src="https://img.shields.io/badge/release-v1.0.0-brightgreen" alt="Release v1.0.0"></a>

## Introduction

SynamediaDataSplitter is a small Java app capable of sorting through the datasets given by Synamedia, to create smaller datasets. Currently, it is capable of separating the dataset in values per servers, and aggregated values by timestamps.

## Changelog
#### v 1.0.0 | Initial release
Features :
- Splits a single data sets into multiple ones, one data per server with its values, and one dataset with the aggregated values per timestamp.
- Using global variables in Main.java, you can modify input file, input directory, the columns in which specific data is located.

## Future releases
- v1.1.0 | Customizable input and output directories, and names, etc
- v2.0.0 | Code restructure to allow the code to be more easily transplanted into another Java program

## Contributors
This app was made by Florian BENMAHDJOUB, reusing parts of [AdairZhao's code](https://github.com/AdairZhao/ARIMA).
