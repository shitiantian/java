##场景：
做java开发，很多后台都是会有文件的上传下载的需求场景，关于文件的操作以及什么样的需求场景对于用什么样的流能达到什么样的效果，是能反映出一个优秀开发者的基本功的。

**下载最多的场景：**
- 1.报表/用户名单/统计之类的数据,根据筛选的条件去查询数据库，然后把查询到的数据生成txt/excel等返回给前端去下载。
- 2.上传到缓存服务器/对象存储服务器上的文件或者图片需要用户下载。

### 下载最多的场景1实现：

**java代码中逻辑：**

首先一般http接口第一步都是先校验，校验参数的合法性。
```java
        //校验查询参数是否合法
```

然后就是根据前端传过来的条件，去数据库查询相关数据。这里需要注意的是：**如果查询的数据量非常大，一定要做成分页查询，分批追加写入文件中。**
```java
        //从数据库中查询出来需要的数据
        List<String> dataList = downloadService.queryAll();
```
将查询出来的数据跟模板的第一行组成数据(考虑数据量是否会jvm溢出)
```java
        //将模板头跟查询的数据组成新list
        List<String> list = new ArrayList<String>();
        list.add("姓名,性别");
        list.addAll(dataList);
```
创建一个临时文件，文件名跟后缀根据具体业务场景具体定义(这里以txt为例子)
```java
        //创建临时文件
        SimpleDateFormat sb = new SimpleDateFormat("yyyyMMdd");
        String date = sb.format(new Date());
        String filename = "下载的文件-"+date;
        String suffix = ".txt";
        File txtFile = File.createTempFile(filename, suffix);
```
创建完文件后，将文件数据写到临时文件里。写的方法有很多，心里必须清楚一两种即可。
```java
        //方案一：将模板标题头+查询到的数据追加到临时文件里
        FileUtils.writeLines(txtFile, list, false);

        //方案二：用PrintWriter流去写
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(txtFile), "utf-8"));
        for (int i = 0; i < list.size(); i++) {
                pw.println(list.get(i));
            }
        pw.flush();
```
到这里，数据库的数据追加到临时文件的功能已经写完了，不管临时文件是txt也好，excel或者pdf也好，都是这种方法，只不过追加内容的写法不同而已。接下来只要通过response的形式将这个临时文件返回给前端就可以了。
```java
//将文本文件以流的形式去下载
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(txtFile));
        //转码，免得文件名中文乱码
        filename = URLEncoder.encode(filename,"UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename+suffix);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data; charset=utf-8");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
```
下载完之后，当调用deleteOnExit()方法时，只是相当于对deleteOnExit（）作一个声明，当程序运行结束，JVM终止时才真正调用deleteOnExit()方法实现删除操作。即该方法是将删除的命令缓存了一下，到服务停止的时候再进行操作！养成这个好习惯就行了。
```java
        //程序退出删除临时文件
        txtFile.deleteOnExit();
```
###  下载最多的场景2实现：
这种情况的下载一般后台不做任何的接口开发,只要是这种的场景的下载，后端只需要在查询的时候给前端返回ossPath(缓存服务器上的文件或者图片地址)，前端可以在浏览器上直接href
加地址实现前端自行下载,但是下载的时候结合实际业务场景，考虑一下像前端跨域问题之类的问题基本就是ok的。

**值得注意的地方:**
- 下载的时候一定要考虑业务场景，水平权限验证一定要考虑，否则只要有http地址根据下载的格式，用个postman之类的工具就可以下载很多本来没权限的文件。
- 下载需要考虑下载的数据量，数据量大的时候一定不要全部查出来放到jvm内存里去写文件里，建议分开写。
- 批量下载的东西最好打个压缩包下载。

**项目地址链接:**

 [文件下载](https://github.com/shitiantian/java/tree/master/src/main/java/com/deanshek/downloadFile)
 
# 写在最后:
- `如果喜欢作者，希望可以给作者一个小星星(star)，让更多的人看到，学习到，共同进步。`
- `所有见解均属于个人理解，如有不对，请各位多多包涵，多多理解！`
