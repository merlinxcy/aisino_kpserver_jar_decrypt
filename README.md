# aisino_kpserver_jar_decrypt
kpserver Jar包解密


decrypt
```
public static void main(String[] args) throws Exception{
        List<String> AllFilePaths = new ArrayList<>();
        String DirPath = "D:\\BaiduNetdiskDownload\\fwkp_install\\fwkp\\WEB-INF\\classes\\com";

        Main.getAllFilePath(DirPath,AllFilePaths);
        for (String path: AllFilePaths){
            System.out.println(path);
            try {
                Main.decrptSingle(path);
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
```
