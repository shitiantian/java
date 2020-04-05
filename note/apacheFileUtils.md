##场景：
这个是apache的关于文件操作的utils,实际开发中有很多很多的场景都用到了,希望您能熟悉这个utils具体都干嘛的，只要跟文件有关的，牢记于心，关键时候避免重复造轮子。

**文件操作最多的场景：**
- 1.后台系统上传了banner图之类的图片或者视频文档等需要存储到缓存服务器/对象存储服务器之类的地方,并且在数据库中记录存放地址的。
- 2.批量上传新增功能，上传csv/txt/excel之类的，直接读取相关数据存入数据库的。
# 目录
- [1.Maven需要引用的相关的包](#1)
- [2.FileUtils读API](#2)
- [3.FileUtils写API](#3)
- [4.FileUtils删除API](#4)
- [5.FileUtils创建API](#5)
- [6.FileUtils复制，移动（剪切)API](#6)
- [7.FileUtils的其他方法](#7)
- [8.对文件的过滤处理](#8)
## **<span id="1">一.Maven需要引用的相关的包</span>**
```java
        <!-- 上传组件包 -->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.4</version>
        </dependency>
```
## **<span id="2">二.FileUtils读API</span>**
读取文本文件的所有行到一个集合  
 ```java
        List<String> lines=FileUtils.readLines(new File("D:/fileUtis/aa.txt"),"utf-8"); 
```
读取文件内容到一个字符串  
 ```java
        String str = FileUtils.readFileToString(new File("D:/fileUtis/aa.txt"), "utf-8"); 
```      
读取文件到一个byte数组  
 ```java
        FileUtils.readFileToByteArray(new File("D:/fileUtis/aa.txt")); 
``` 
## **<span id="3">三.FileUtils写API</span>**
将字符写入到一个文件，文件不存在会创建；第三个参数：true：追加，false：覆盖  
 ```java
         FileUtils.write(new File("D:/fileUtis/targetFile/aa.txt"),"aa",false);  
``` 
根据指定编码将字符写入到一个文件，文件不存在会创建；会覆盖    
 ```java
        FileUtils.write(new File("D:/fileUtis/targetFile/bb.txt"),"ickes","utf-8");  
``` 
根据指定编码将字符写入到一个文件，文件不存在会创建；会覆盖   
 ```java
        FileUtils.write(new File("D:/fileUtis/targetFile/cc.txt"),"ickes","utf-8",true);  
``` 
将一个字符串集合根据指定的分隔符写入到文件中，第四个参数是分隔符   
 ```java
        List<String> ss = new ArrayList<>();  
        ss.add("aa");ss.add("bb");  
        FileUtils.writeLines(new File("D:/fileUtis/targetFile/dd.txt"),"utf-8",ss, ","); 
``` 
将一个字符串集合，一行一行写入到文件中 
 ```java
        FileUtils.writeLines(new File("D:/fileUtis/targetFile/ee.txt"), "utf-8", ss,true);
``` 
## **<span id="4">四.FileUtils删除API</span>**
删除一个目录和他的所有子目录，如果文件或者目录不存在会抛出异常  
 ```java
        FileUtils.deleteDirectory(new File("D:/fileUtis/targetFile/"));
``` 
删除一个目录或者一个文件，如果这个目录或者目录不存在不会抛出异常  
 ```java
        FileUtils.deleteQuietly(new File("D:/fileUtis/targetFile/"));  
``` 
清除一个目录下面的所有文件跟目录 
 ```java
        FileUtils.cleanDirectory(new File("D:/fileUtis/targetFile/")); 
``` 
删除一个文件，如果是目录则递归删除forceDelete(File file),跟deleteDirectory基本一样  
 ```java
        FileUtils.forceDelete(new File("D:/fileUtis/targetFile/"));   
``` 
## **<span id="5">五.FileUtils创建API</span>**
创建一个目录，可以递归创建，只要不为null   
 ```java
        FileUtils.forceMkdir(new File("D:/fileUtis/targetFile/aa"));  
``` 
创建一个空文件，若文件应经存在则只更改文件的最近修改时间   
 ```java
        FileUtils.touch(new File("D:/fileUtis/targetFile/Liftoff.java"));  
``` 
## **<span id="6">六.FileUtils复制，移动（剪切)API</span>**
复制目录   
 ```java
        File dataFile=new File("D:/fileUtis/dataFile");  
        File targetFile = new File("D:/fileUtis/targetFile");  
        if(targetFile.isDirectory()){//判断是否是一个目录  
            FileUtils.copyDirectory(dataFile,targetFile);  
        }  
``` 
复制文件  
 ```java
        dataFile=new File("D:/fileUtis/dataFile/joiner.java");  
        targetFile = new File("D:/fileUtis/targetFile/aa.txt");  
        FileUtils.copyFile(dataFile,targetFile);  
        //复制文件到一个目录  
        dataFile=new File("D:/fileUtis/dataFile/joiner.java");  
        targetFile = new File("D:/fileUtis/");  
        FileUtils.copyFileToDirectory(dataFile, targetFile);
``` 
移动目录到新的目录并且删除老的目录,新的目录不存在会创建，如果存在会报错  
 ```java
        dataFile=new File("D:/fileUtis/dataFile");  
        targetFile = new File("D:/fileUtis/aa");  
        FileUtils.moveDirectory(dataFile, targetFile);  
``` 
把目录移动到一个新的文件下面，是新文件下面,ture，当目标文件不存在是否创建  
 ```java
        dataFile=new File("D:/fileUtis/dataFile");  
        targetFile = new File("D:/fileUtis/aa");  
        FileUtils.moveDirectoryToDirectory(dataFile, targetFile, true);  
``` 
复制文件  
```java
        FileUtils.moveFile(srcFile, destFile);
``` 
## **<span id="7">七.FileUtils的其他方法</span>**
获取一个目录的大小
 ```java
        FileUtils.sizeOfDirectory(file); 
``` 
获取文件或者目录的大小 
 ```java
        FileUtils.sizeOf(file);
``` 
得到系统临时目录的路径，例如C:\Users\ADMINI~1\AppData\Local\Temp\
 ```java
        FileUtils.getTempDirectoryPath() 
``` 
比较两个文件内容是否相等，左右两边有空格返回false  
 ```java
        FileUtils.contentEquals(file1, file2)；
``` 
获取用户的主目录路径,返回的是字符串    
 ```java
        FileUtils.getUserDirectoryPath();   
``` 
获取代表用户主目录的文件，返回的是file  
 ```java
        FileUtils.getUserDirectory();  
``` 
根据指定的文件获取一个新的文件输出流    
 ```java
        FileUtils.openOutputStream(file); 
``` 
字节转换成直观带单位的值（包括单位GB，MB，KB或字节），如下返回95 M     
 ```java
        FileUtils.byteCountToDisplaySize(100000000);    
``` 
## **<span id="8">八.对文件的过滤处理</span>**
通配符过滤目录下的文件     
 ```java
        File dir = new File("D:/fileUtis");    
        FileFilter fileFilter = new WildcardFileFilter("*.java");    
        File[] files = dir.listFiles(fileFilter);    
        for (int i = 0; i < files.length; i++) {    
           System.out.println(files[i]);    
        }    
``` 
过滤文件大小，等于或大于某一尺寸 ，单位为字节     
 ```java
        File dir = new File("D:/fileUtis");   
        String[] files = dir.list( new SizeFileFilter(1024*2) );  
        for ( int i = 0; i < files.length; i++ ) {  
            System.out.println(files[i]);  
        }  
``` 
过滤文件后缀名    
 ```java
        File dir = new File("D:/fileUtis");   
        String[] files = dir.list(new SuffixFileFilter(".java"));  
        for (int i = 0; i < files.length; i++) {    
        System.out.println(files[i]);    
        }  
``` 
使用正则表达式过滤     
 ```java
        File dir = new File("D:/fileUtis");   
        FileFilter fileFilter = new RegexFileFilter("^j.*.java");    
        File[] files = dir.listFiles(fileFilter);    
        for (int i = 0; i < files.length; i++) {    
          System.out.println(files[i]);    
        }  
``` 
文件前缀过滤    
 ```java
        File dir = new File("D:/fileUtis");  
        String[] files = dir.list( new PrefixFileFilter("aa"));    
        for ( int i = 0; i < files.length; i++ ) {    
             System.out.println(files[i]);    
        }    
``` 
打印这个目录下所有.java结尾的文件名,会递归去他子目录中去找 
 ```java
        File dir = new File("D:/fileUtis");  
        Collection<File> files = FileUtils.listFiles(dir,  
                FileFilterUtils.suffixFileFilter(".java"),    
                DirectoryFileFilter.DIRECTORY);  
         for (File f : files) {    
              System.out.println(f.getName());    
         }   
``` 

**地址链接:**
- [apache的FileUtils的API](http://commons.apache.org/proper/commons-io/apidocs/org/apache/commons/io/FileUtils.html)
- [相关地址](https://blog.csdn.net/liangwenmail/article/details/79278973)
 
# 写在最后:
- `如果喜欢作者，希望可以给作者一个小星星(star)，让更多的人看到，学习到，共同进步。`
- `所有见解均属于个人理解，如有不对，请各位多多包涵，多多理解！`
