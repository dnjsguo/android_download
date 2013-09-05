package com.appdear.client.utility;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LogcatScanner {
    private static AndroidLogcatScanner scannerThead;

    public final static void startScanLogcatInfo(LogcatObserver observer) {
        if (scannerThead == null) {
            scannerThead = new AndroidLogcatScanner(observer);
            scannerThead.start();
        }
    }

    static class AndroidLogcatScanner extends Thread {
        private LogcatObserver observer;
        public AndroidLogcatScanner(LogcatObserver observer) {
            this.observer = observer;
        }

        public void run() {
            String[] cmds = { "logcat", "-c" };
            String shellCmd = "logcat";
            Process process = null;
            InputStream is = null;
            DataInputStream dis = null;
            String line = "";
            Runtime runtime = Runtime.getRuntime();
            try {
                observer.handleNewLine(line);
                int waitValue;
                waitValue = runtime.exec(cmds).waitFor();
                observer.handleNewLine("waitValue=" + waitValue + "\n Has do Clear logcat cache.");
                process = runtime.exec(shellCmd);
                is = process.getInputStream();
                dis = new DataInputStream(is);
                while ((line = dis.readLine()) != null) {
                	if (observer != null)
                		observer.handleNewLine(line);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException ie) {
                ie.printStackTrace();
            } finally {
                try {
                    if (dis != null) {
                        dis.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (process != null) {
                        process.destroy();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}