##场景：
做java开发，很多后台都是会有文件的上传下载的需求场景，关于文件的操作以及什么样的需求场景对于用什么样的流能达到什么样的效果，是能反映出一个优秀开发者的基本功的。

**上传最多的场景：**
- 1.后台系统上传了banner图之类的图片或者视频文档等需要存储到缓存服务器/对象存储服务器之类的地方,并且在数据库中记录存放地址的。
- 2.批量上传新增功能，上传csv/txt/excel之类的，直接读取相关数据存入数据库的。

### 上传最多的场景1实现：

**使用文件上传首先maven项目中要引用2个文件上传的包：**
```java
        <!-- 上传组件包 -->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.1</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.4</version>
        </dependency>
```
**SpringMVC 中，文件的上传，是通过 MultipartResolver 实现的。实现文件的上传，需要在 spring-mvc.xml 中注册相应的 MultipartResolver:**
```xml
    <bean id="multipartResolver"
		  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<property name="defaultEncoding" value="utf-8"></property>
		<property name="maxUploadSize" value="104857600"></property>
	</bean>
```
**java代码中逻辑：**
获取到上传的文件之后，首先根据具体业务场景先做各种检验：
- 文件后缀校验：根据具体业务场景不同，严格控制需要上传的文件后缀名
- 文件大小校验:根据业务需求，考虑下上传文件大小限制，一般实际上为了带宽之类的，小图不超过200k大图不超过2M等具体业务具体分析。
- 安全校验:比如上传的文件内容是否是涉黄的、是否里边是木马脚本、上传的图片是否有覆盖的图层之类的，具体需要自己分析下
```java
        // 获取上传文件的名称
        String filename = upload.getOriginalFilename();

        //校验文件后缀是否符合业务场景要求PNG/JPG/JPEG/TXT/XLS/XLSX/DOC等等后缀根据条件
        boolean b = FilenameUtils.isExtension(filename,"jpg");
        if(!b){
            return "文件名非法";
        }
```
各种校验完之后，文件需要存储的一般都是设置一个文件临时存放路径，将文件存放到临时路径，然后在存放到存储服务器上，如果直接上传文件放入数据库那种的，就不用这一步操作了。
```java
        // 上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads/");
        // 判断，该路径是否存在
        File file = new File(path);
        if(!file.exists()){
            // 创建该文件夹
            file.mkdirs();
        }
```
创建完临时文件夹之后，为了防止上传的重复，跟文件便于管理，一般都是给文件重命名，文件命名可以有意义，比如项目缩写加业务缩写加上传日期加序列编号,或者没意义的随机也行，看业务场景是否需要。
```java
        // 把文件的名称设置唯一值，uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        filename = uuid+"_"+filename;
```
做好一切准备之后，就可以把文件放到本地临时文件夹中了。
```java
        //文件上传到本地
        upload.transferTo(new File(path,filename));
```
上传到临时文件夹中之后，实际公司是不会把文件存到服务运行的机器上的。然后一般都是调用对象存储服务器的api,将文件上传上去，并且返回对象存储服务器的地址。
```java
        //将本地临时文件上传到缓存服务器/对象存储服务器，比如阿里云oss里
        String ossPath = "";
        try {
            ossPath = ossUtils.upload(new File(path,filename));
        } catch (Exception e) {
            return "上传oss失败.具体原因:"+e;
        }
```
因为是写的小例子，实际公司中return都是返回一个固定好的跟前端交互的实体类转化的json，一般都包括：
- flag(是否成功的状态)
- code(响应码，200-成功 201-oss存储失败 202-系统错误之类的状态码)
- msg(响应码对应的描述信息)
- data(响应成功之后需要跟前端交互的数据都放这个里边)

大家不要学我随便return一个字符串回去就完事了。

上传oss缓存服务器成功之后，临时文件就可以删除了。
```java
        //将临时文件夹中的内容删除掉
        if(file.exists()){
            file.delete();
        }
```
最后一般上传成功之后都是会有一张表中存关于上传的一些数据的，具体实体类的具体字段根据业务去分析！
```java
        //将文件服务器的地址存到数据库里
        UploadFile  uploadFile = new UploadFile();
        uploadFile.setOssPath(ossPath);
        uploadService.insert(uploadFile);
```
然后最后就可以给前端返回200-成功的消息了，用于最后的前端逻辑交互。
```java
        return "上传文件成功";
```
### 上传最多的场景2实现：
这种情况大多数都是批量处理用的，举个例子：有一批用户签到加积分的活动，然后需要给用户批量加积分，这个时候就会批量上传一个excel，里边A列是userId(用户唯一id)，B列是num(添加的积分数),然后读excel
批量新增或者更新到数据库。这一段设计到excel/word等打开，在之后excel章节会具体举例子，这里就不重复了。

**值得注意的地方:**
- 校验一定要考虑清楚
- 上传属于开发的基本功，必须跟背1234一样背熟，因为用到的场景太多了。

**项目地址链接:**


# 写在最后:
- `如果喜欢作者，希望可以给作者一个小星星(star)，让更多的人看到，学习到，共同进步。`
- `所有见解均属于个人理解，如有不对，请各位多多包涵，多多理解！`
