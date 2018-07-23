package com.nupday.util;

import com.nupday.config.DBInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

@Service
public class DBUtil {


    /**
     * 备份数据库操作
     *
     * @return BackupInfo备份状态对象
     * @throws IOException 文件读写错误
     */
    public static File backup(DBInfo dbConfig) throws IOException, InterruptedException {
        File mysqlFile = getMysqlDump();
        if (mysqlFile == null) {
            throw new FileNotFoundException("在lib路径下没有找到名为'mysqldump'的mysql工具,MysqlDumpUtil类的功能依赖此文件!");
        }
        String filePath;
        if (isWindows()) {
            filePath = "E:\\backup\\db\\backup." + new Date().getTime();
        } else {
            filePath = "/var/nupday/backup/db/backup." + new Date().getTime();
        }
        File saveFile = new File(filePath);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
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
                .append(" ").append(dbConfig.getDatabase());

        Runtime rt = Runtime.getRuntime();
        // 调用mysqldump的cmd命令
        final Process process;
        if (isWindows()) {
            process = rt.exec(new String[]{"cmd", "/c", mysqlFile.getPath() + cmdStr.toString()});
        } else {
            process = rt.exec(new String[]{"sh", "-c", "cd " + mysqlFile.getParent() + " | mysqldump " + cmdStr.toString()});
        }
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(saveFile), "utf8"));
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line = bufferedReader.readLine())!= null){
                printWriter.println(line);
            }
            printWriter.flush();
            if(process.waitFor() == 0){//0 表示线程正常终止。
                return saveFile;
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saveFile;

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
