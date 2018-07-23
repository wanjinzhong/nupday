package com.nupday.util;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.nupday.config.DBInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class DBUtil {

    private final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    /**
     * 备份数据库操作
     *
     * @return BackupInfo备份状态对象
     * @throws IOException 文件读写错误
     */
    public static InputStream backup(DBInfo dbConfig) throws IOException, InterruptedException {
        File f = getMysqlDump();
        if (f == null) {
            throw new FileNotFoundException("在lib路径下没有找到名为'mysqldump'的mysql工具,MysqlDumpUtil类的功能依赖此文件!");
        }

        //构建远程数据库命令字符串  
        StringBuilder cmdStr = new StringBuilder();
        cmdStr.append(" -u")
              .append(dbConfig.getUserName())
              .append(" -p")
              .append(dbConfig.getPassword())
              .append(" -h")
              .append(dbConfig.getHost())
              .append(" -P")
              .append(dbConfig.getPort())
              .append("  --ignore-table=")
              .append(dbConfig.getDatabase()).append(".t_backup_database ")
              .append(" ").append(dbConfig.getDatabase());

        Runtime rt = Runtime.getRuntime();
        // 调用mysqldump的cmd命令  
        final Process p;
        if (isWindows()) {
            p = rt.exec(new String[] {"cmd", "/c", f.getPath() + cmdStr.toString()});
        } else {
            p = rt.exec(new String[] {"sh", "-c", "cd " + f.getParent() + " | mysqldump " + cmdStr.toString()});
        }

        //得到远程数据库备份的流文件
        InputStream in = p.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line1 = null;
        while ((line1 = reader.readLine()) != null) {
            if (line1 != null) {
                stringBuilder.append(line1).append("\r\n");
            } else {
                break;
            }
        }
        logger.info(stringBuilder.toString());
        return new ByteArrayInputStream(stringBuilder.toString().getBytes());
    }

    /**
     * 获取mysqldump工具所的位置
     *
     * @return 名为"mysqldump.exe"的文件路径
     */
    private static File getMysqlDump() throws FileNotFoundException {
        File f = null;
        try {
            if (isWindows()) {
                f = new File(ResourceUtils.getURL("classpath:mysql/mysqldump.exe").getPath());
            } else {
                f = new File(ResourceUtils.getURL("classpath:mysql/mysqldump").getPath());
            }

        } catch (NullPointerException e) {
        }

        return f;
    }

    /**
     * 获取cmd命令执行的错误信息
     *
     * @param p Process对象
     * @return 错误信息字符串
     * @throws IOException 文件读写错误
     */
    private static String getErrMsg(Process p) throws IOException {
        StringBuilder errMsg = new StringBuilder();
        InputStream in = p.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf8"));
        for (String inStr = null; (inStr = br.readLine()) != null; ) {
            errMsg.append(inStr).append("<br>");
        }
        in.close();
        br.close();
        return errMsg.length() == 0 ? null : errMsg.toString();
    }

    /**
     * 判断是不是windows平台
     *
     * @return true是Windows平台, false非Windows平台
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").indexOf("Windows") != -1;
    }

}
