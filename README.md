# LuceneDemo


## 使用方法
1. 用Eclipse导入该项目
2. 设置CreateIndex.java中的创建索引位置indexPath、待处理数据位置dataPath，并运行CreateIndex.java
3. 设置Lucene.java中使用索引位置 indexPath
4. 运行swing.java，即可。

## 文件结构

├─image
|  ├─save.png 
|  └─search.png 
└─src 
   ├─CreateIndex.java
   ├─Lucene.java
   ├─LuceneTest.java
   ├─swing.java
   └─testMySQL.java 

## 注意事项
1. eclipse 下文件默认为GBK编码
2. dataPath下的数据最好是ANSI编码，且每行为一条记录。





