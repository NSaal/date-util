# Date-util

## Base
基于Hutool的date部分重新裁剪封装，删除了一些个人不常用的方法和类。

保留了大部分方法，具体可参阅Hutool官方文档[日期时间工具-DateUtil](https://hutool.cn/docs/#/core/%E6%97%A5%E6%9C%9F%E6%97%B6%E9%97%B4/%E6%97%A5%E6%9C%9F%E6%97%B6%E9%97%B4%E5%B7%A5%E5%85%B7-DateUtil)。

核心静态工具类[DateUtil.java](src%2Fmain%2Fjava%2Fdatetool%2FDateUtil.java)。

常用方法
- `format(Date,...)`，Date2String
- `parse(CharSequence,...)`，String2Date
- `offset...(Date,...)`，时间日期偏移
- 等等

## New
加入个人常用的[DateStyle.java](src%2Fmain%2Fjava%2Fdatetool%2Fcore%2Fenums%2FDateStyle.java)。
> 但删除了`YYYY_MM_01("yyyy-MM-01")`和`YYYYMM01("yyyyMM01")`两个没什么用还危险的格式。

新增两个方法
- `DateUtil#format(Date, DateStyle)`
- `DateUtil#parse(CharSequence, DateStyle)。`

# TODO 

- [ ] 字符串格式转换的一系列方法`String2String`
- [ ] ...