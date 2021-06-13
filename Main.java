import java.io.*;
import java.net.URLClassLoader;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Key;
import java.security.KeyFactory;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import java.math.BigInteger;


public class Main {
    public static byte[] ReadFile(String filename) throws Exception{
        BufferedInputStream bis = null;
        bis = new BufferedInputStream(new FileInputStream(filename));
        int len = bis.available();
        byte []b = new byte[len];
        bis.read(b,0,len);
        bis.close();
        return b;
    }

    public static void decrptSingle(String filename) throws Exception{
        /*
        必须是绝对路路径
         */
//        String filename = "D:\\BaiduNetdiskDownload\\fwkp_install\\fwkp\\WEB-INF\\classes\\com\\aisino\\fwkp\\web\\action\\ChangePWDAction.class";
//        String filename = filename;
        String output = filename;
        Key key = null;
        Cipher rsa_dec = null;
        byte[] src;
        src = Main.ReadFile(filename);
        byte[] key_mw = new byte[128];
        byte[] data_mw = new byte[src.length - 128];
        System.arraycopy(src, 0, key_mw, 0, 128);
        System.arraycopy(src, 128, data_mw, 0, data_mw.length);

        String N = "105743618554227505632899035061456967840542166626218258197709917285318689447389680249648408326955945917112826706436362206987979751131661655067809047178779254188313381199216051711944125790293119643761675098504431709171157564006374673674486812092957897957451487633477280208245378228817411894748479581980200168789";
        String e = "65537";
        if (key == null)
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger big_N = new BigInteger(N);
            BigInteger big_e = new BigInteger(e);
            KeySpec keySpec = new RSAPublicKeySpec(big_N, big_e);
            key = keyFactory.generatePublic(keySpec);
        }
        if (rsa_dec == null)
        {
            rsa_dec = Cipher.getInstance("RSA");
            rsa_dec.init(2, key);
        }
        byte[] key_src = rsa_dec.doFinal(key_mw);

        byte[] key_tmp = new byte[16];
        byte[] iv_tmp = new byte[16];
        System.arraycopy(key_src, 0, key_tmp, 0, 16);
        System.arraycopy(key_src, 16, iv_tmp, 0, 16);

        Cipher aes_dec = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aes_dec.init(2, new SecretKeySpec(key_tmp, "AES"), new IvParameterSpec(iv_tmp));

        src = aes_dec.doFinal(data_mw);

        DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
        out.write(src);
        out.close();


    }

    public static List<String> getAllFilePath(String DirPath,List<String> AllFilePaths) throws Exception{
        File dirfile = new File( DirPath );
        File[] files = dirfile.listFiles();
        if (files == null){
            return AllFilePaths;
        }
        for(int i=0;i < files.length; i++){
            File file = files[i];
            if(file.isDirectory()){
                getAllFilePath(file.getAbsolutePath(),AllFilePaths);

            }
            else{
                AllFilePaths.add(file.getPath());
            }
        }

        return AllFilePaths;
    }
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
}
